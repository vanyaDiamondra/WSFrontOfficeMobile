package vae.vae.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vae.vae.db.ConnectionPostgresSQL;
import vae.vae.model.Enchere;
import vae.vae.model.MiseEnchere;
import vae.vae.model.Utilisateurs;
import vae.vae.repository.MiseEnchereRepository;
import vae.vae.service.DataResponse;
import vae.vae.service.MiseEnchereService;

import java.sql.Connection;
import java.util.List;

@Controller
@CrossOrigin("*")
@RequestMapping("/miseenchere")
public class MiseEnchereController {

    @Autowired
    MiseEnchereRepository miseEnchereRepository;

    @RequestMapping("/encherir")
    @GetMapping
    public ResponseEntity<DataResponse> BidAction(@RequestBody Utilisateurs utilisateurs) throws Exception{
        DataResponse dr = new DataResponse();
        if( utilisateurs.getId() == 0 ){
            dr.setStatus("500");
            dr.setData("login.html");
        } else if( utilisateurs != null ){
            dr.setStatus("200");
            dr.setData("encherir.html");
        }
        return ResponseEntity.accepted().body(dr);
    }

    @RequestMapping("/addMise")
    @PostMapping
    public void createMiseEnchere(@RequestBody MiseEnchere e) throws Exception{
        Connection conn = ConnectionPostgresSQL.getconnect();
        MiseEnchereService service = new MiseEnchereService();
        Enchere enchere = new Enchere(e.getEnchereID());
        enchere = enchere.find(conn);

        try {
            service.newMiseEnchere(conn, enchere, e, miseEnchereRepository);
        }
        catch (Exception ex){
            throw ex;
        }
        finally {
            conn.close();
        }
    }

    @RequestMapping("/historiqueEnchere")
    @GetMapping
    public ResponseEntity<DataResponse> getAllMiseByEnchere(@RequestBody Enchere enchere){
       List<MiseEnchere> miseEncheres = miseEnchereRepository.findAllMiseEnchereByEnchereID(enchere.getId());

       DataResponse dr = new DataResponse();
       dr.setStatus("200");
       dr.setData(miseEncheres);

       return ResponseEntity.accepted().body(dr);
    }

}

