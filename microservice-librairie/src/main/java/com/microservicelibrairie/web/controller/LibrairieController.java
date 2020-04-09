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


    @Autowired
   private LibrairieRepository librairieRepository;
    @Autowired
    private LivreRepository livreRepository;
    @Autowired
    private UserReservationDao userReservationDao;
    @Autowired
    private GenresRepository genresRepository;
    @Autowired
    private LivreReserveAttenteDao livreReserveAttenteDao;
    @Autowired
    private UserProxy userProxy;
    @Autowired
    private JavaMailSender javaMailSender;





    @Value("${dir.images}")
    private String imageDir;


    /**
     * Liste de tous les livres en librairie
     */
    @GetMapping(value = "/librairieAll")
    public List<Librairie>listeDesLivresAll(){
        return librairieRepository.findAll();
    }

    /**
     * Liste de tous les livres réservés
     */
    @GetMapping(value = "/userReservation")
    public  List<UserReservation>userReservations(){
        return userReservationDao.findAll();
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
     * recherche des livres en location par utilisateur
     * @param num
     * @return
     */
    @GetMapping(value = "/location")
    public List<LivreReserve> findByLocation(@RequestParam(name = "num") long num){
        return livreRepository.findByIdClient(num);
    }

    @GetMapping(value = "/locationDteMax")
    public Date dateLocationMax(@RequestParam(name = "id") long id){
        List<LivreReserve> livresLocation=livreRepository.findByLibrairie_Id(id) ;
        Date dateMax =null;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        for (LivreReserve newLivre : livresLocation) {
            dateMax = livresLocation.get(0).getDateFin();
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
     * Rechercher des livres par genre
     * @param genre nom du genre
     * @return
     */
    @GetMapping(value = "/genre")
    public List<Librairie> findByGenre(  @RequestParam(name = "genre",defaultValue =" " )String genre){

        return librairieRepository.findByGenre_Genre(genre);

    }

    /**
     * Afficher tous les livres réservés
     */

    @GetMapping(value = "locationAll")
    public List<LivreReserve>livreReservesAll(){
        return livreRepository.findAll();
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

    /**
     * prolonger un livre de 4 semaines
     * @param id livre
     */

    @PutMapping(value ="/prolongation")
    public void prolongation(@RequestParam(name = "id") Long id) {
        LivreReserve prolongation= livreRepository.findById(id).get();

        Calendar cal = Calendar.getInstance();
        cal.setTime(prolongation.getDateFin());
        cal.add(Calendar.MONTH,1);
        prolongation.setDateFin(cal.getTime());
        prolongation.setProlongation(true);
        livreRepository.save(prolongation);
    }

    /**
     * Permet de mettre à jour la reservation d'un livre
     * @param livreReserve
     */
    @PutMapping(value ="/modifListeReserve")
    public void modifListeReserve(@RequestBody LivreReserve livreReserve) {

        livreRepository.save(livreReserve);
    }

    @GetMapping(value = "/ChercheLocation")
    public Boolean chercheLocation(@RequestParam(name = "num") long num,
                                   @RequestParam(name = "idLivre") long idLivre) {
        List<LivreReserve> livresLocation = livreRepository.findByIdClient(num);

        for (LivreReserve livreReserve : livresLocation) {

            if (livreReserve.getLibrairie().getId().equals(recupererUnLivre(idLivre))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Enregistrer la reservation d'un livre
     * @param livreReserve
     * @param idLivre id du livre
     * @param idUser id de l'utilisateur
     */

    @PostMapping(value ="saveReservation/{idLivre}/{idUser}" )
    public LivreReserve saveReservation(@RequestBody LivreReserve livreReserve, @PathVariable("idLivre") Long idLivre,
                                        @PathVariable("idUser") Long idUser) {


        Librairie livre= recupererUnLivre(idLivre).get();

        List<LivreReserveAttente> listAttente=livreAttenteIdLivre(idLivre);

       for (LivreReserveAttente livreAttente : listAttente ){
        if (livreAttente.getIdClient().equals(idUser)&livreAttente.getMailEnvoye() ){
            deletePreReservation(livreAttente.getId());
            livre.setNExemplaire(livre.getNExemplaire()+1);
        }}

        if (livre.getNExemplaire()<=0)throw new ImpossibleAjouterUneReservationException("Ce livre n'est plus" +
                " disponible");

        Date dateJour= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateJour);
        cal.add(Calendar.MONTH,1);


        List<LivreReserveAttente>list2=livreAttenteClient(idUser);
        for (LivreReserveAttente livreReserveAttente : list2) {
            if (livreReserveAttente.getLibrairie().equals(livre)) {
                deletePreReservation(livreReserveAttente.getId());
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
        livreReserve.setDateDeb(dateJour);
        livreReserve.setDateFin(cal.getTime());
        livreReserve.setIdClient(idUser);
        livreReserve.setLibrairie(livre);
        livreReserve.setProlongation(false);
        livreReserve.setUserReservation(userReservation);
        livre.setNExemplaire(livre.getNExemplaire()-1);
        userReservation.setNbLivre(userReservation.getNbLivre()+1);

        return livreRepository.save(livreReserve);

    }


    /**
     * Supprimer la resevation d'un livre
     * @param id da la reservation.
     */
    @DeleteMapping (value ="deleteReservation/{id}" )
    public void deleteReservation(@PathVariable("id") Long id) {
        LivreReserve livreReserves=livreRepository.findById(id).get();
        Librairie livre=librairieRepository.findById(livreReserves.getLibrairie().getId()).get();
        //on fait -1 au nombre de livre que le client à en sa possession
        livreReserves.getUserReservation().setNbLivre(livreReserves.getUserReservation().getNbLivre()-1);
       List<LivreReserveAttente>list=livreReserveAttenteDao.findAll();
       if(list.size()>0){
        mail(livreReserves.getLibrairie().getId());
       }else {
           livre.setNExemplaire(livre.getNExemplaire()+1);
           librairieRepository.save(livre);

       }



        //on supprime le livre de la liste des livres loués
        livreRepository.deleteById(id);

        // si le client à plus de livre reservé on le supprime de la
        // liste des clients qui ont un livre en leurs possession.

        if (livreReserves.getUserReservation().getNbLivre()==0){
            userReservationDao.delete(livreReserves.getUserReservation());
        }
    }

    private void mail(@PathVariable("id") Long id) {

        Librairie livre= recupererUnLivre(id).get();
        List<LivreReserveAttente> list=livreReserveAttenteDao.findAll();

        Date dateJour= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateJour);
        cal.add(Calendar.HOUR,48);

        for (LivreReserveAttente reserveAttente : list) {
            if (reserveAttente.getLibrairie().equals(livre) & reserveAttente.getNlistAttente() == 1) {
                reserveAttente.setDateMail(cal.getTime());
                sendEmail(reserveAttente.getId());
                reserveAttente.setMailEnvoye(true);
                livreReserveAttenteDao.save(reserveAttente);
            }
        }
    }


    @PostMapping(value ="savePreReservation/{idLivre}" )
    public LivreReserveAttente savePreReservation( @PathVariable("idLivre") Long idLivre,
                                                   @RequestParam(name ="idUser") Long idUser
                                                   ) {

        LivreReserveAttente livreReserveAttente=new LivreReserveAttente();
        Librairie livre=recupererUnLivre(idLivre).get();
      if (livreReserveClient(idUser, idLivre)){
          return null;
      }

        livreReserveAttente.setIdClient(idUser);
        livreReserveAttente.setLibrairie(livre);
        livreReserveAttente.setMailEnvoye(false);
        livreReserveAttente.setDateRetour(dateLocationMax(idLivre));
        livreReserveAttente.setNlistAttente(livre.getPrereserve()+1);
        livre.setPrereserve(livre.getPrereserve()+1);

        return livreReserveAttenteDao.save(livreReserveAttente);

    }

    public Boolean livreReserveClient(long idClient, long idLivre){
        Librairie livre=recupererUnLivre(idLivre).get();

        List<LivreReserveAttente>list=livreAttenteClient(idClient);
        Boolean livrereserveAttente=false;
        Boolean livrereserve =false;
        for (LivreReserveAttente reserveAttente : list) {
            if (reserveAttente.getLibrairie().equals(livre)) {
                return livrereserveAttente=true;
            }
        }
        List<LivreReserve> listLivreReserve=findByLocation(idClient);
        for (LivreReserve livreReserve : listLivreReserve) {
            if (livreReserve.getLibrairie().equals(livre)) {
                return livrereserve=true;
            }
        }

        return livrereserve || livrereserveAttente;

    }

    /**
     * Supprimer la Pre-resevation d'un livre
     * @param id da la Pre-reservation.
     */
    @DeleteMapping (value ="deletePreReservation/{id}" )
    public void deletePreReservation(@PathVariable("id") Long id) {
        LivreReserveAttente livreReserveAttente=livreAttente(id).get();
        livreReserveAttente.getLibrairie().setPrereserve(livreReserveAttente.getLibrairie().getPrereserve()-1);

       if(livreReserveAttente.getMailEnvoye()){
           Librairie livre=librairieRepository.findById(livreReserveAttente.getLibrairie().getId()).get();
           livreReserveAttente(id);

           List<LivreReserveAttente> list=livreReserveAttenteDao.findAll();
           if ((list.size()==0)){
               livre.setNExemplaire(livre.getNExemplaire()+1);
               librairieRepository.save(livre);
           }
           for (LivreReserveAttente attenteList : list) {
               if(!attenteList.getLibrairie().equals(livreReserveAttente.getLibrairie())){
                   livre.setNExemplaire(livre.getNExemplaire()+1);
                   librairieRepository.save(livre);
               }
           }
       }else {
        livreReserveAttente(id);
       }
    }

    private void livreReserveAttente(@PathVariable("id") Long id) {
        LivreReserveAttente livreReserveAttente=livreAttente(id).get();
        List<LivreReserveAttente> list=livreReserveAttenteDao.findAll();
        livreReserveAttenteDao.delete(livreReserveAttente);
        for (LivreReserveAttente attenteList : list) {
            if (attenteList.getLibrairie().equals(livreReserveAttente.getLibrairie()) &
                    livreReserveAttente.getNlistAttente() < attenteList.getNlistAttente()) {
                attenteList.setNlistAttente(attenteList.getNlistAttente() - 1);
                livreReserveAttenteDao.save(attenteList);
                mail(livreReserveAttente.getLibrairie().getId());
            }
        }
    }


    /**
     * Rechercher un livre qui est en attente  par son Id.
     * @param id  livre
     */

    @GetMapping(value = "/livreAttente")
    public Optional<LivreReserveAttente>livreAttente(@RequestParam(name="id",defaultValue = " ")long id){

        Optional<LivreReserveAttente>livre=livreReserveAttenteDao.findById(id);
        if(!livre.isPresent()) throw new LivreNotFoundException("Ce livre n'existe pas");
        return livre;
    }

    @GetMapping(value = "/livreAttenteIdLivre")
    public List<LivreReserveAttente>livreAttenteIdLivre(@RequestParam(name="id",defaultValue = " ")long id){
        List<LivreReserveAttente>livre=livreReserveAttenteDao.findByLibrairie_Id(id);
        return livre;
    }

    /**
     * recherche des livres en location par utilisateur
     * @param num
     * @return
     */
    @GetMapping(value = "/livreAttenteClient")
    public List<LivreReserveAttente> livreAttenteClient(@RequestParam(name = "num") long num){
        return livreReserveAttenteDao.findByIdClient(num);
    }

    @GetMapping(value = "/livreAttenteAll")
    public List<LivreReserveAttente> livreAttenteAll(){
        return livreReserveAttenteDao.findAll();
    }


    private void sendEmail(long idReservation) {

        SimpleDateFormat formater = null;
        Date dateJour= new Date();
        formater = new SimpleDateFormat("'le' dd/MM/yyyy");
        Optional<LivreReserveAttente> livreReserveAttenteList=livreReserveAttenteDao.findById(idReservation);
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
