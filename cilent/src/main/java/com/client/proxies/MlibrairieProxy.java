package com.client.proxies;



import com.client.bean.GenreBean;
import com.client.bean.LibrairieBean;
import com.client.bean.ReserverLivreBean;
import com.client.bean.EmprunterLivreBean;
import com.client.controller.LibraryResponse;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@FeignClient(name="zuul-server")
@RibbonClient("microservice-librairie")

public interface MlibrairieProxy {

 @GetMapping(value = "/microservice-librairie/librairieAll")
 List<LibrairieBean> listDesLivresAll();

 @GetMapping(value = "/microservice-librairie/emprunterAll")
 List<EmprunterLivreBean> emprunterAll();
 @GetMapping(value = "/empruntId")
 Optional<EmprunterLivreBean> empruntId(@RequestParam(name = "id") long id);

 @PostMapping(value ="/microservice-librairie/saveEmprunt/{idLivre}/{idUser}" )
 EmprunterLivreBean saveEmprunt(@PathVariable("idLivre") Long idLivre,
                                    @PathVariable("idUser") Long idUser);


 @GetMapping(value = "/microservice-librairie/empruntIdDteMax")
 Date empruntIdDteMax(@RequestParam(name = "id") long id);


 @PutMapping(value = "/microservice-librairie/prolongation")
 LibrairieBean prolongation(@RequestParam(name = "id") long id);

 @PutMapping(value = "/microservice-librairie/modifListEmprunts")
 EmprunterLivreBean modifListEmprunts(@RequestBody EmprunterLivreBean emprunterLivreBean);

 @GetMapping(value = "/microservice-librairie/emprunt")
 List<EmprunterLivreBean> findByEmprunt(@RequestParam(name = "num") long num);

 @GetMapping(value = "/microservice-librairie/librairie")
 LibrairieBean recupererUnLivre(@RequestParam(name="id",defaultValue = " ")long id);

 @PostMapping(value = "/microservice-librairie/librairies")
 LibrairieBean saveLivre(@RequestBody LibrairieBean livre);

 @PutMapping(value = "/microservice-librairie/modif")
 LibrairieBean updatelivre(@RequestBody LibrairieBean librairie);

 @DeleteMapping(value = "/microservice-librairie/delete/{id}")
 LibrairieBean deletelivre(@PathVariable("id") Long id);

 @RequestMapping(value = "getPhoto", produces = MediaType.IMAGE_JPEG_VALUE)
 @ResponseBody
 byte[] getPhoto(String id);


 @GetMapping(value = "/microservice-librairie/librairies")
 LibraryResponse listDesLivres(@RequestParam(name = "motClefAuteur", defaultValue = "") String motClefAuteur,
                               @RequestParam(name = "motClefTitre", defaultValue = "") String motClefTitre
                               , @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "8") int size);


 @GetMapping(value = "/microservice-librairie/genreAll")
 List<GenreBean> genreLivreAll();


 @GetMapping(value = "/microservice-librairie/genre")
  List<LibrairieBean> findByGenre(@RequestParam(name = "genre",defaultValue =" " ) String genre);

 @GetMapping(value = "/microservice-librairie/genre/{id}")
 Optional<GenreBean> GenreLivre(@PathVariable("id") int id);


 @PostMapping(value ="/microservice-librairie/saveReservation/{idLivre}" )
 ReserverLivreBean savePreReservation(@PathVariable("idLivre") Long idLivre,
                                      @RequestParam(name = "idUser") Long idUser);

 @DeleteMapping (value ="/microservice-librairie/deleteReservation/{id}" )
 ReserverLivreBean deleteReservation(@PathVariable("id") Long id);

 @GetMapping(value = "/microservice-librairie/livreReserve")
 Optional<ReserverLivreBean>livreReserve(@RequestParam(name="id",defaultValue = " ")long id);

 @GetMapping(value = "/microservice-librairie/livreAttenteClient")
 List<ReserverLivreBean> livreReserverClient(@RequestParam(name = "num") long num);


}





