package com.microservicelibrairie.web.controller;


import com.microservicelibrairie.dao.*;
import com.microservicelibrairie.entities.*;
import com.microservicelibrairie.web.exceptions.GenreNotFoundException;
import com.microservicelibrairie.web.exceptions.ImpossibleAjouterUnLivreException;
import com.microservicelibrairie.web.exceptions.ImpossibleAjouterUneReservationException;
import com.microservicelibrairie.web.exceptions.LivreNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
public class LibrairieController {


    @Autowired
    LibrairieRepository librairieRepository;
    @Autowired
    LivreRepository livreRepository;
    @Autowired
    UserReservationDao userReservationDao;
    @Autowired
    GenresRepository genresRepository;
    @Autowired
    LivreReserveAttenteDao livreReserveAttenteDao;


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
        Page<Librairie>Pagelivres= librairieRepository.findByAuteurContainingIgnoreCaseAndTitreContainingIgnoreCase(
                motClefAuteur,motClefTitre,PageRequest.of(page,size));
        return Pagelivres;
    }

    /**
     * recherche des livres en location par utilisateur
     * @param num
     * @return
     */
    @GetMapping(value = "/location")
    public List<LivreReserve> findByLocation(@RequestParam(name = "num") long num){
        List<LivreReserve> livresLocation=livreRepository.findByIdClient(num) ;
        return livresLocation;
    }

    @GetMapping(value = "/locationDteMax")
    public Date dateLocationMax(@RequestParam(name = "id") long id){
        List<LivreReserve> livresLocation=livreRepository.findByLibrairie_Id(id) ;
        Date dateMax = null;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        for (int i =0; i < livresLocation.size(); i++){
           LivreReserve newLivre= livresLocation.get(i);
            dateMax =livresLocation.get(1).getDateFin();
            cal2.setTime(newLivre.getDateFin());
            cal1.setTime(dateMax);
            cal1.add(Calendar.MONTH,3);
            if(cal1.after(cal2)) {
                dateMax=newLivre.getDateFin();
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
        List<Librairie>Genrelivres= librairieRepository.findByGenre_Genre(genre);

        return Genrelivres;

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
        Optional<Librairie>livre=librairieRepository.findById(id);
        if(!livre.isPresent()) throw new LivreNotFoundException("Ce livre n'existe pas");
        return livre;
    }


    /**
     * Enregistrer un nouveau livre.
     * @param livre
     * @return
     */
    @PostMapping(value = "/librairies")
    public ResponseEntity<Librairie>saveLivre(@RequestBody Librairie livre){
        livre.setPrereserveMax(livre.getNExemplaire()*2);
        livre.setPrereserve(0);
        Librairie saveLivre = librairieRepository.save(livre);
        if(saveLivre == null) throw new ImpossibleAjouterUnLivreException("Impossible d'ajouter ce livre");

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

        for (int i = 0; i < livresLocation.size(); i++) {

            if (livresLocation.get(i).getLibrairie().getId().equals(recupererUnLivre(idLivre))) {
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
        if (livre.getNExemplaire()<=0)throw new ImpossibleAjouterUneReservationException("Ce livre n'est plus" +
                " disponible");

        Date dateJour= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateJour);
        cal.add(Calendar.MONTH,1);

        List<UserReservation> list=userReservationDao.findAll();
        boolean userinscrit = false;

        for (int i =0; i < list.size(); i++){
            UserReservation userlist=list.get(i);

            if (userlist.getIdClient().compareTo(idUser)==0){
                userinscrit=true;
            }
        }

        if (userinscrit==false){
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
        Librairie livre= recupererUnLivre(livreReserves.getLibrairie().getId()).get();
        livre.setNExemplaire(livre.getNExemplaire()+1);
        livreReserves.getUserReservation().setNbLivre(livreReserves.getUserReservation().getNbLivre()-1);
        livreRepository.deleteById(id);

        if (livreReserves.getUserReservation().getNbLivre()==0){
            userReservationDao.delete(livreReserves.getUserReservation());
        }
    }

    @PostMapping(value ="savePreReservation/{idLivre}" )
    public LivreReserveAttente savePreReservation( @PathVariable("idLivre") Long idLivre,
                                                   @RequestParam(name ="idUser") Long idUser) {
        LivreReserveAttente livreReserveAttente=new LivreReserveAttente();
        Librairie livre=recupererUnLivre(idLivre).get();
        livreReserveAttente.setIdClient(idUser);
        livreReserveAttente.setLibrairie(livre);
        livreReserveAttente.setDateRetour(dateLocationMax(idLivre));
        livreReserveAttente.setNlistAttente(livre.getPrereserve()+1);
        livre.setPrereserve(livre.getPrereserve()+1);

        return livreReserveAttenteDao.save(livreReserveAttente);

    }

    /**
     * Supprimer la Pre-resevation d'un livre
     * @param id da la Pre-reservation.
     */
    @DeleteMapping (value ="deletePreReservation/{id}" )
    public void deletePreReservation(@PathVariable("id") Long id) {
       LivreReserveAttente livreReserveAttente=livreAttente(id).get();
       livreReserveAttente.getLibrairie().setPrereserve(livreReserveAttente.getLibrairie().getPrereserve()-1);
       livreReserveAttenteDao.delete(livreReserveAttente);


      List<LivreReserveAttente> list=livreReserveAttenteDao.findAll();

        for (int i =0; i < list.size(); i++){
            LivreReserveAttente attenteList=list.get(i);

            if (attenteList.getLibrairie().equals(livreReserveAttente.getLibrairie())&
                    livreReserveAttente.getNlistAttente()<attenteList.getNlistAttente()){
                attenteList.setNlistAttente(attenteList.getNlistAttente()-1);
                livreReserveAttenteDao.save(attenteList);
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

    /**
     * recherche des livres en location par utilisateur
     * @param num
     * @return
     */
    @GetMapping(value = "/livreAttenteClient")
    public List<LivreReserveAttente> livreAttenteClient(@RequestParam(name = "num") long num){
        List<LivreReserveAttente> livreAttenteClient=livreReserveAttenteDao.findByIdClient(num) ;
        return livreAttenteClient;
    }



}
