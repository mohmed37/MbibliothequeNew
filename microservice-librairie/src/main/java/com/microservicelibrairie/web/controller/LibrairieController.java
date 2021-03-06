package com.microservicelibrairie.web.controller;



import com.microservicelibrairie.bean.UserBean;
import com.microservicelibrairie.dao.*;
import com.microservicelibrairie.entities.*;
import com.microservicelibrairie.proxies.UserProxy;
import com.microservicelibrairie.web.exceptions.GenreNotFoundException;
import com.microservicelibrairie.web.exceptions.ImpossibleAjouterUneReservationException;
import com.microservicelibrairie.web.exceptions.LivreNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class LibrairieController {

// ==================== Attributs ====================

    @Autowired
   private LibrairieRepository librairieRepository;
    @Autowired
    private EmprunterRepository emprunterRepository;
    @Autowired
    private UserReservationDao userReservationDao;
    @Autowired
    private GenresRepository genresRepository;
    @Autowired
    private LivreReserveRepository livreReserveRepository;
    @Autowired
    private UserProxy userProxy;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${dir.images}")
    private String imageDir;




    // ==================== Méthodes livres disponibles en bibliothéque====================
    /**
     * Liste de tous les livres en librairie
     */
    @GetMapping(value = "/librairieAll")
    public List<Librairie>listeDesLivresAll(){
        return librairieRepository.findAll();
    }


    /**
     * liste des livre
     * @param motClefAuteur rechercher un auteur par mot clé.
     * @param motClefTitre  rechercher un titre par mot clé.
     */
    @GetMapping(value = "/librairies")
    public Page<Librairie> listDesLivres(@RequestParam(name="page",defaultValue = "0")int page,
                                         @RequestParam(name="size",defaultValue = "8")int size,
                                         @RequestParam(name = "motClefAuteur",defaultValue ="") String motClefAuteur,
                                         @RequestParam(name = "motClefTitre",defaultValue ="") String motClefTitre)
    {
        return librairieRepository.findByAuteurContainingIgnoreCaseAndTitreContainingIgnoreCase(
                motClefAuteur,motClefTitre,PageRequest.of(page,size));
    }

    /**
     * Rechercher un livre par son Id.
     * @param id  livre
     */

    @GetMapping(value = "/librairie")
    public Optional<Librairie>recupererUnLivre(@RequestParam(name="id",defaultValue = " ")long id){
        Optional<Librairie> livre = librairieRepository.findById(id);
        if(!livre.isPresent()) throw new LivreNotFoundException("Ce livre n'existe pas");
        return livre;
    }


    /**
     * Enregistrer un nouveau livre.
     * @param livre
     * @return
     */
    @PostMapping(value = "/librairies")
    public ResponseEntity<Librairie>saveLivre(@RequestBody Librairie livre, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return null;
        }
        livre.setPrereserveMax(livre.getNExemplaire()*2);
        livre.setPrereserve(0);
        Librairie saveLivre = librairieRepository.save(livre);

        return new ResponseEntity<Librairie>(saveLivre, HttpStatus.CREATED);
    }


    /**
     *  Permet de mettre à jour l'etat d'un livre
     * @param livre
     */
    @PutMapping(value = "/librairies")
    public void updatelivre(@RequestBody Librairie livre) {
        librairieRepository.save(livre);

    }

    /**
     * Supprimer un livre.
     * @param id livre
     */

    @DeleteMapping(value = "/librairies/{id}")
    public void deletelivre(@PathVariable("id") Long id){
        librairieRepository.deleteById(id);
    }

    // ==================== Méthodes livres empruntés ====================

    /**
     * Liste de tous les livres empruntés par utilisateur
     */
    @GetMapping(value = "/userReservation")
    public  List<UserReservation>userReservations(){
        return userReservationDao.findAll();
    }


    /**
     * Afficher tous les livres emprutés
     */

    @GetMapping(value = "emprunterAll")
    public List<Emprunt>emprunterAll(){
        return emprunterRepository.findAll();
    }


    /**
     * recherche des livres en emprunts par utilisateur
     * @param num
     * @return
     */
    @GetMapping(value = "/emprunt")
    public List<Emprunt> findByemprunt(@RequestParam(name = "num") long num){
        return emprunterRepository.findByIdClient(num);
    }

    /**
     * recherche des livres en emprunts par id
     * @param id
     * @return
     */

    @GetMapping(value = "/empruntId/{id}")
    public Optional<Emprunt>empruntId(@PathVariable("id") long id){
        Optional<Emprunt>empreint= emprunterRepository.findById(id);
        if(!empreint.isPresent()) throw new GenreNotFoundException("Ce numéro d'empreint n'existe pas");
        return empreint;
    }
    /**
     * Retourne la date Max des livres en emprunts
     * @param id
     * @return
     */

    @GetMapping(value = "/empruntIdDteMax")
    public Date empruntIdDteMax(@RequestParam(name = "id") long id){
        List<Emprunt> livresEmpruntes= emprunterRepository.findByLibrairie_Id(id) ;
        Date dateMax =null;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        for (Emprunt newLivre : livresEmpruntes) {
            dateMax = livresEmpruntes.get(0).getDateFin();
            cal2.setTime(newLivre.getDateFin());
            cal1.setTime(dateMax);
            cal1.add(Calendar.MONTH, 3);
            if (cal1.after(cal2)) {
                dateMax = newLivre.getDateFin();
            }
        }
        return dateMax;
    }
    /**
     * Permet de mettre à jour l'emprunt d'un livre
     * @param emprunt
     */
    @PutMapping(value ="/modifListEmprunts")
    public void modifListeEmprunts(@RequestBody Emprunt emprunt) {
        emprunterRepository.save(emprunt);
    }

    /**
     * recherche des livres en emprunts par utilisateur
     * @param num
     *  @param idLivre
     * @return
     */

    @GetMapping(value = "/ChercheEmprunt")
    public Boolean ChercheEmprunt(@RequestParam(name = "num") long num,
                                  @RequestParam(name = "idLivre") long idLivre) {
        List<Emprunt> livresEmpruntes = emprunterRepository.findByIdClient(num);

        for (Emprunt emprunt : livresEmpruntes) {

            if (emprunt.getLibrairie().getId().equals(recupererUnLivre(idLivre))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enregistrer l'emprunt d'un livre
     * @param idLivre id du livre
     * @param idUser id de l'utilisateur
     */

    @PostMapping(value ="/saveEmprunt/{idLivre}/{idUser}" )
    public Emprunt saveEmprunt(@PathVariable("idLivre") Long idLivre,
                               @PathVariable("idUser") Long idUser) {
        Emprunt emprunt =new Emprunt();
        Librairie livre= recupererUnLivre(idLivre).get();
        List<ReserverLivre> listAttente=livreAttenteIdLivre(idLivre);

        for (ReserverLivre livreAttente : listAttente ){
            if (livreAttente.getIdClient().equals(idUser)&livreAttente.getMailEnvoye() ){
                deleteReservation(livreAttente.getId());
                livre.setNExemplaire(livre.getNExemplaire()+1);
            }}

        if (livre.getNExemplaire()<=0)throw new ImpossibleAjouterUneReservationException("Ce livre n'est plus" +
                " disponible");

        Date dateJour= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateJour);
        cal.add(Calendar.MONTH,1);

        List<ReserverLivre>list2=livresReservesClient(idUser);
        for (ReserverLivre reserverLivre : list2) {
            if (reserverLivre.getLibrairie().equals(livre)) {
                deleteReservation(reserverLivre.getId());
            }
        }

        List<UserReservation> list=userReservationDao.findAll();
        boolean userinscrit = false;

        for (UserReservation userlist : list) {
            if (userlist.getIdClient().compareTo(idUser) == 0) {
                userinscrit = true;
                break;
            }
        }

        if (!userinscrit){
            UserReservation userReservation=new UserReservation();
            userReservation.setIdClient(idUser);
            userReservationDao.save(userReservation);
        }

        UserReservation userReservation= userReservationDao.findByIdClient(idUser);
        emprunt.setDateDeb(dateJour);
        emprunt.setDateFin(cal.getTime());
        emprunt.setIdClient(idUser);
        emprunt.setLibrairie(livre);
        emprunt.setProlongation(false);
        emprunt.setUserReservation(userReservation);
        livre.setNExemplaire(livre.getNExemplaire()-1);
        userReservation.setNbLivre(userReservation.getNbLivre()+1);

        return emprunterRepository.save(emprunt);

    }


    /**
     * Supprimer l'emprunt d'un livre
     * @param id da la reservation.
     */
    @DeleteMapping (value ="deleteEmprunt/{id}" )
    public void deleteEmprunt(@PathVariable("id") Long id) {
        Emprunt livreReserves= emprunterRepository.findById(id).get();
        Librairie livre=librairieRepository.findById(livreReserves.getLibrairie().getId()).get();
        //on fait -1 au nombre de livre que le client à en sa possession
        livreReserves.getUserReservation().setNbLivre(livreReserves.getUserReservation().getNbLivre()-1);
        List<ReserverLivre>list= livreReserveRepository.findAll();
        if(list.size()>0){
            mail(livreReserves.getLibrairie().getId());
        }else {
            livre.setNExemplaire(livre.getNExemplaire()+1);
            librairieRepository.save(livre);

        }
        //on supprime le livre de la liste des livres loués
        emprunterRepository.deleteById(id);
        // si le client à plus de livre reservé on le supprime de la
        // liste des clients qui ont un livre en leurs possession.
        if (livreReserves.getUserReservation().getNbLivre()==0){
            userReservationDao.delete(livreReserves.getUserReservation());
        }
    }

    // ==================== Méthodes genres de livre====================

    /**
     * Rechercher des livres par genre
     * @param genre nom du genre
     * @return
     */
    @GetMapping(value = "/genre")
    public List<Librairie> findByGenre(  @RequestParam(name = "genre",defaultValue =" " )String genre){
        return librairieRepository.findByGenre_Genre(genre);
    }


    /**
     * Afficher tous les genres
     */

    @GetMapping(value="genreAll")
    public  List<Genre>genreLivreAll(){
        return genresRepository.findAll();
    }

    /**
     * rechercher un genre par son Id.
     * @param id genre
     */

    @GetMapping(value = "/genre/{id}")
    public Optional<Genre>GenreLivre(@PathVariable("id") int id){
        Optional<Genre>genre=genresRepository.findById(id);
        if(!genre.isPresent()) throw new GenreNotFoundException("Ce genre de livre n'existe pas");
        return genre;
    }

    // ==================== Méthodes pour la prolongation====================
    /**
     * prolonger un livre de 4 semaines
     * @param id livre
     */

    @PutMapping(value ="/prolongation")
    public void prolongation(@RequestParam(name = "id") Long id) {

        Emprunt prolongation= emprunterRepository.findById(id).get();

        //J'ai mis une exception pour une erreur sur la demande de prolongation.
        if(prolongation.getProlongation()) throw new RuntimeException("La prolongation à dejà était réalisée");
        if(prolongation.getDateFin().before(new Date())) throw new RuntimeException("Date de fin est dépassée");

        Calendar cal = Calendar.getInstance();
        cal.setTime(prolongation.getDateFin());
        cal.add(Calendar.MONTH,1);
        prolongation.setDateFin(cal.getTime());
        prolongation.setProlongation(true);
        emprunterRepository.save(prolongation);
    }


    // ==================== Méthodes  pour la réservation d'un livre indisponible====================

    /**
     * Enregistrer un livre réservé par un utilisateur
     * @param idUser
     *  @param idLivre
     * @return
     */
    @PostMapping(value ="saveReservation/{idLivre}" )
    public ReserverLivre saveReservation(@PathVariable("idLivre") Long idLivre,
                                            @RequestParam(name ="idUser") Long idUser
                                                   ) {

        ReserverLivre reserverLivre =new ReserverLivre();
        Librairie livre=recupererUnLivre(idLivre).get();

        if (livre.getNExemplaire()>0)throw new ImpossibleAjouterUneReservationException("Ce livre est disponible " +
                "impossible de faire une préréservation");
        if (livre.getPrereserveMax()<=livre.getPrereserve())throw new ImpossibleAjouterUneReservationException("Impossble" +
                " de faire une réservation le Maximum est atteint");

      if (livreReserveClient(idUser, idLivre)){
          return null;
      }

        reserverLivre.setIdClient(idUser);
        reserverLivre.setLibrairie(livre);
        reserverLivre.setMailEnvoye(false);
        reserverLivre.setDateRetour(empruntIdDteMax(idLivre));
        reserverLivre.setNlistAttente(livre.getPrereserve()+1);
        livre.setPrereserve(livre.getPrereserve()+1);

        return livreReserveRepository.save(reserverLivre);

    }

    private Boolean livreReserveClient(long idClient, long idLivre){
        Librairie livre=recupererUnLivre(idLivre).get();

        List<ReserverLivre>list=livresReservesClient(idClient);
        Boolean livrereserveAttente=false;
        Boolean livrereserve =false;
        for (ReserverLivre reserveAttente : list) {
            if (reserveAttente.getLibrairie().equals(livre)) {
                return livrereserveAttente=true;
            }
        }
        List<Emprunt> listEmprunt =findByemprunt(idClient);
        for (Emprunt emprunt : listEmprunt) {
            if (emprunt.getLibrairie().equals(livre)) {
                return livrereserve=true;
            }
        }

        return livrereserve || livrereserveAttente;

    }

    /**
     * Supprimer la résevation d'un livre
     * @param id
     */
    @DeleteMapping (value ="deleteReservation/{id}" )
    public void deleteReservation(@PathVariable("id") Long id) {
        ReserverLivre reserverLivre =livreReserve(id).get();
        reserverLivre.getLibrairie().setPrereserve(reserverLivre.getLibrairie().getPrereserve()-1);

       if(reserverLivre.getMailEnvoye()){
           Librairie livre=librairieRepository.findById(reserverLivre.getLibrairie().getId()).get();
           livreReserveAttente(id);

           List<ReserverLivre> list= livreReserveRepository.findAll();
           if ((list.size()==0)){
               livre.setNExemplaire(livre.getNExemplaire()+1);
               librairieRepository.save(livre);
           }
           for (ReserverLivre attenteList : list) {
               if(!attenteList.getLibrairie().equals(reserverLivre.getLibrairie())){
                   livre.setNExemplaire(livre.getNExemplaire()+1);
                   librairieRepository.save(livre);
               }
           }
       }else {
        livreReserveAttente(id);
       }
    }

    /**
     * Rechercher un livre réservé
     * @param id
     * @return
     */

    private void livreReserveAttente(@PathVariable("id") Long id) {
        ReserverLivre reserverLivre =livreReserve(id).get();
        livreReserveRepository.delete(reserverLivre);
        List<ReserverLivre> list= livreReserveRepository.findByLibrairie_Id(reserverLivre.getLibrairie().getId());
        for (ReserverLivre attenteList : list) {
            if (reserverLivre.getNlistAttente() < attenteList.getNlistAttente()) {
                attenteList.setNlistAttente(attenteList.getNlistAttente() - 1);
                livreReserveRepository.save(attenteList);
                if(list.size()>1){
                mail(reserverLivre.getLibrairie().getId());}
            }
        }
    }


    /**
     * Rechercher un livre reservé par son Id.
     * @param id  livre
     */

    @GetMapping(value = "/livreReserve")
    public Optional<ReserverLivre>livreReserve(@RequestParam(name="id",defaultValue = " ")long id){

        Optional<ReserverLivre>livre= livreReserveRepository.findById(id);
        if(!livre.isPresent()) throw new LivreNotFoundException("Ce livre n'existe pas");
        return livre;
    }
    /**
     * Rechercher un livre reservé par son Id du livre
     * @param id  livre
     */

    @GetMapping(value = "/livreAttenteIdLivre")
    public List<ReserverLivre>livreAttenteIdLivre(@RequestParam(name="id",defaultValue = " ")long id){
        List<ReserverLivre>livre= livreReserveRepository.findByLibrairie_Id(id);
        return livre;
    }

    /**
     * recherche un livre reservé  par utilisateur
     * @param num
     * @return
     */
    @GetMapping(value = "/livreAttenteClient")
    public List<ReserverLivre> livresReservesClient(@RequestParam(name = "num") long num){
        return livreReserveRepository.findByIdClient(num);
    }

    /**
     * Rechercher de tous les  livres reservés
     * @return
     */

    @GetMapping(value = "/livreAttenteAll")
    public List<ReserverLivre> livreAttenteAll(){
        return livreReserveRepository.findAll();
    }

    // ==================== Méthodes pour l'envoie de mail ====================

    /**
     * Envoie de mail à un utilisateur qui à réservé un livre
     *  @param id
     */

    private void mail(@PathVariable("id") Long id) {

        Librairie livre= recupererUnLivre(id).get();
        List<ReserverLivre> list= livreReserveRepository.findAll();

        Date dateJour= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateJour);
        cal.add(Calendar.HOUR,48);

        for (ReserverLivre reserveAttente : list) {
            if (reserveAttente.getLibrairie().equals(livre) & reserveAttente.getNlistAttente() == 1) {
                reserveAttente.setDateMail(cal.getTime());
                sendEmail(reserveAttente.getId());
                reserveAttente.setMailEnvoye(true);
                livreReserveRepository.save(reserveAttente);
            }
        }
    }

    /**
     * Envoie de mail à un utilisateur qui à réservé un livre
    */

    private void sendEmail(long idReservation) {

        SimpleDateFormat formater = null;
        Date dateJour= new Date();
        formater = new SimpleDateFormat("'le' dd/MM/yyyy");
        Optional<ReserverLivre> livreReserveAttenteList= livreReserveRepository.findById(idReservation);
       UserBean client=userProxy.findById(livreReserveAttenteList.get().getIdClient()).get();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(client.getEmail());

        msg.setSubject("Bibliothèque de Tours - Courrier de rappel");
        msg.setText("Bibliothèque Municipale de Tours\n" +
                "    2bis AV. ANDRE MALRAUX\n" +
                "    37042 TOURS CEDEX\n" +
                "    02 47 05 47 33\n" +
                "    secretariat@bm-tours.fr\n" +
                "\n" +
                "\n" +
                "                                                  M. "+" "+ client.getNom()+" "
                + client.getPrenom()+"\n" +
                "\n" +
                "                                                  Tours," +formater.format(dateJour)+"\n" +
                "\n" +
                "\n" +
                "\n" +
                "    Madame, Monsieur, \n" +
                "\n" +
                "    Nous sommes heureux de vous informé que le livre que vous avez reservé est diponible \n" +
                "    à l'accueil de votre bibliothéque.\n"+
                "\n" +
                "    Merci de recuperer sans tarder le "+livreReserveAttenteList.get().getLibrairie().getGenre().getGenre()+" " +
                " qui a pour titre "+ livreReserveAttenteList.get().getLibrairie().getTitre()+".\n" +
                "\n" +
                "    Vous avez 48 heures pour le recuper faute de quoi votre reservation sera  annulée \n" +
                "\n" +
                "    Comptant sur votre réativité et restant à votre disposition pour tout renseignement relatif\n" +
                "      disposition pour tout renseignement relatif aux prêts, je vous prie d'agréer mes\n" +
                "      sincères salutations.\n" +
                "\n" +
                "                                        La Directrice de la Bibliothèque.\n" +
                "\n" +
                "\n");
        javaMailSender.send(msg);}

}
