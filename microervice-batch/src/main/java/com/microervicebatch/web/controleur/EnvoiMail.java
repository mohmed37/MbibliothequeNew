package com.microervicebatch.web.controleur;

import com.microervicebatch.bean.LivreReserveBean;
import com.microervicebatch.bean.UserBean;
import com.microervicebatch.bean.UserReservationBean;
import com.microervicebatch.proxies.MlibrairieProxy;
import com.microervicebatch.proxies.MuserProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class EnvoiMail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private  MlibrairieProxy mlibrairieProxy;
    @Autowired
    private MuserProxy muserProxy;

    /**
     * envoi du mail de relance en automatique.
     */
    @Scheduled(cron = "0  33 17 * * * ")
    public void sendEmail() {

        SimpleDateFormat formater = null;
        Date dateJour= new Date();
        formater = new SimpleDateFormat("'le' dd/MM/yyyy");

        List<UserReservationBean> list=mlibrairieProxy.userReservations();

        for (int i =0; i < list.size(); i++){
            UserReservationBean userReservation=list.get(i);
            UserBean userBean=muserProxy.findById(userReservation.getIdClient()).get();
            List<LivreReserveBean>listVivreUser=mlibrairieProxy.findByLocation(userReservation.getIdClient());

            List<LivreReserveBean> listeLivreDateExpiree=listVivreUser.stream()
                    .filter((LivreReserveBean livreReservation) -> dateJour.after(livreReservation.getDateFin()))
                    .collect(Collectors.toList());

            if (!listeLivreDateExpiree.isEmpty()){
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(userBean.getEmail());

                msg.setSubject("Bibliothèque de Tours - Courrier de rappel");
                msg.setText("Bibliothèque Municipale de Tours\n" +
                        "    2bis AV. ANDRE MALRAUX\n" +
                        "    37042 TOURS CEDEX\n" +
                        "    02 47 05 47 33\n" +
                        "    secretariat@bm-tours.fr\n" +
                        "\n" +
                        "\n" +
                        "                                                  M. "+" "+ userBean.getNom()+" "
                        + userBean.getPrenom()+"\n" +
                        "\n" +
                        "                                                  Tours," +formater.format(dateJour)+"\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "    Madame, Monsieur, \n" +
                        "\n" +
                        "    Malgré notre précedent rappel, vous n'avez toujours pas, à ce jour, restitué les\n" +
                        "      documents ci-dessous.\n" +
                        "    Merci de les rendre sans tarder, afin que d'autres lecteurs puissent en profiter.\n" +
                        "    Tant que vous ne les aurez pas restitués, il ne vous sera pas possible d'emprunter\n" +
                        "      d'autres documents, et ce dans n'importe quelle bibliothèque du réseau.\n" +
                        "\n" +
                        "    Si vous ne retrouvez plus les documents qui vous sont demandés, vous avez la\n" +
                        "      possibilité de les remplacer par un exemplaire identique.\n" +
                        "\n" +
                        "    Si ce courrier vous parvient après le retour des documents réclamés, merci de ne\n" +
                        "      pas en tenir compte. Pensez, pour vos prochains emprunts, à ne pas dépasser les\n" +
                        "      durées de prêt.\n" +
                        "    Sinon, conformément à l'article 2 de l'arrêté 462/04 portant règlement intérieur\n" +
                        "      de la Bibliothèque, passés les 2 mois de retard, vous recevrez une facture\n" +
                        "      correspondant à la valeur des documents non rendus.\n" +
                        "    Le règlement devra s'effectuer directement auprès de la Bibliothèque (paiement par\n" +
                        "      chèque à l'ordre de M. le Trésorier Principal de Tours Municipale).\n" +
                        "\n" +
                        "    Comptant sur une régularisation rapide de votre situation et restant à votre\n" +
                        "      disposition pour tout renseignement relatif aux prêts, je vous prie d'agréer mes\n" +
                        "      sincères salutations.\n" +
                        "\n" +
                        "                                        La Directrice de la Bibliothèque.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        " =====================================================\n" +
                        afficherLivres(listeLivreDateExpiree) +
                        "\n" +
                        "\n" +
                        "\n");
                javaMailSender.send(msg);}
        }

    }

    /**
     * Afficher la liste des livres réservés et en retard pour l'envoi du mail.
     * @param livres
     */
    private String afficherLivres(List<LivreReserveBean> livres) {
        String chaineAfficher = new String();

        for (LivreReserveBean l: livres) {
            chaineAfficher += l.afficher();
        }
        return chaineAfficher;
    }

}
