package com.client.controller;


import com.client.bean.*;
import com.client.proxies.MlibrairieProxy;
import com.client.proxies.MuserProxy;
import com.client.service.IUserService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class ClientController {

    @Autowired
    MlibrairieProxy mlibrairieProxy;
    @Autowired
    MuserProxy muserProxy;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    IUserService userService;

    @Value("${dir.images}")
    private String imageDir;

    Logger log = LoggerFactory.getLogger(this.getClass());
    private Model model;

    /**
     * page d'accueil
     * @param motClefAuteur rechercher un auteur par mot clé.
     * @param motClefTitre  rechercher un titre par mot clé.
     */
    @RequestMapping("/")
    public String accueil(Model model,@RequestParam(name = "motClefAuteur",defaultValue ="") String motClefAuteur,
                          @RequestParam(name = "motClefTitre",defaultValue ="") String motClefTitre
            ,@RequestParam(name="page",defaultValue = "0")int page,
                          @RequestParam(name="size",defaultValue = "8")int size) {
        log.info("Envoi requête vers microservice-produits");
        LibraryResponse pageLivres = mlibrairieProxy.listDesLivres( motClefAuteur,motClefTitre,page,size);
        int pagesCount1=pageLivres.getContent().size();
        int[]pages=new int[pagesCount1];
        for (int j=0;j<pagesCount1;j++) pages[j]=j;
        model.addAttribute("pages",pages);
        model.addAttribute("pageLivres", pageLivres.getContent());
        model.addAttribute("pageCourant",page);
        List<GenreBean>genres=mlibrairieProxy.genreLivreAll();
        model.addAttribute("genres",genres);

        return "Accueil";
    }

    /**
     *
     * @param genre  rechercher par genre.
     */
    @RequestMapping("/selectionParGenre")
    public String selectionParGenre(Model model,@RequestParam(name = "genre",defaultValue =" ") String genre) {
        log.info("Envoi requête vers microservice-produits");
        List<LibrairieBean> pageLivres = mlibrairieProxy.findByGenre(genre);
        model.addAttribute("pageLivres", pageLivres);
        List<GenreBean>genres=mlibrairieProxy.genreLivreAll();
        model.addAttribute("genres",genres);

        return "Accueil";
    }

    /**
     * page connection utilisateur
     */

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }



    /**
     * @param id rechercher un livre par son Id.
     */

    @RequestMapping("/detailLivre")
    public String detailLivre(Model model,@RequestParam(name="id",defaultValue = " ")long id){
        LibrairieBean detailLivre = mlibrairieProxy.recupererUnLivre(id);
        model.addAttribute("detailLivre",detailLivre);
        Date dateRetour=mlibrairieProxy.empruntIdDteMax(id);
        model.addAttribute("dateRetour",dateRetour);
        UserBean userConnec=userService.getUserConnec();
        model.addAttribute("userConnect",userConnec);
        return "detailLivre";


    }


    /**
     * page compte client(ces informations et les livres reservés).
     */
    @RequestMapping("/userLocation")
    public String user(Model model) {
        log.info("Envoi requête vers microservice-utilisateur");

        UserBean userConnec=userService.getUserConnec();
        model.addAttribute("userConnect", userConnec);

        List<EmprunterLivreBean> livresLocation = mlibrairieProxy.findByEmprunt(userConnec.getNum());
        model.addAttribute("livresLocation", livresLocation);
        List<ReserverLivreBean> reserverLivreBeanList =mlibrairieProxy.livreReserverClient(userConnec.getNum());
        model.addAttribute("livreAttentes", reserverLivreBeanList);

        Date dateJour=new Date();
        model.addAttribute("dateJour",dateJour);
        return "user";
    }

    /**
     * acces au formulaire pour enregistrer un  nouveau livre en base de donnée.
     */

    @RequestMapping("/form")
    public String formLivre(Model model){
        LibrairieBean livre =new LibrairieBean();
        model.addAttribute("livre",livre);
        UserBean userConnec=userService.getUserConnec();
        model.addAttribute("userConnect",userConnec);
        List<GenreBean> genres=mlibrairieProxy.genreLivreAll();
        model.addAttribute("genres",genres);
        return "formLivre";
    }

    /**
     * Enregistrement du nouveau livre
     * @param livre sauvegarde d'un livre
     * @param file  sauvegarde de la plaquette.
     * @param iDgenre sauvegarde du genre de livre.
     */

    @RequestMapping("/save")
    public String saveLivre(@Valid @ModelAttribute("livre")LibrairieBean livre,
                            @RequestParam(name = "picture") MultipartFile file, @RequestParam("iDgenre") int iDgenre, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return "formLivre";
        }
        livre.setGenre(mlibrairieProxy.GenreLivre(iDgenre).get());

        livre = mlibrairieProxy.saveLivre(livre);
        if(!(file.isEmpty())){
            livre.setPhoto(file.getOriginalFilename());
            file.transferTo(new File(imageDir+livre.getId()));
        }
        livre.setPrereserveMax(livre.getNExemplaire()*2);
        livre.setPrereserve(0);
        mlibrairieProxy.saveLivre(livre);
        return "redirect:/form";

        /**
         * Acces à la plaquette du livre
         */

    }
    @RequestMapping(value = "/getPhoto",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhoto(String id) throws IOException {
        File f=new File(imageDir+id);
        return IOUtils.toByteArray(new FileInputStream(f));

    }

    /**
     * @param username rechercher un utilisateur avec sans username.
     */
    @RequestMapping(value ="/username")
    public UserBean findUserByUsername(@RequestParam(name = "username",defaultValue ="") String username) {
        return muserProxy.findUserByUsername(username);}


    /**
     * prolonger le livre de 4 semaines avec son Id.
     * @param id
     */
    @RequestMapping(value = "/prolongation/{id}")
    public String prolongation(@PathVariable("id") long id){
        mlibrairieProxy.prolongation(id);

        return "redirect:/userLocation";
    }

    @RequestMapping(value ="/reservation/{idLivre}")
    public String reservation(@PathVariable("idLivre") long idLivre){
        UserBean idUser=userService.getUserConnec();
        mlibrairieProxy.savePreReservation(idLivre,idUser.getNum());

        return "redirect:/detailLivre?id="+idLivre;
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        mlibrairieProxy.deleteReservation(id);
        return "redirect:/userLocation";
    }



}
