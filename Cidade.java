package com.aps.aps.Controller;

import com.aps.aps.ControllerRequest.GerenciaAPI;
import com.aps.aps.Helpers.FuncBasicas;
import com.aps.aps.Model.MedidaModel;
import com.aps.aps.Model.RespostaModel;
import com.aps.aps.ResponseModel.CitiesResponse;
import com.aps.aps.ResponseModel.LatestMeasurementResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/Cidade")
public class Cidade {

    private final FuncBasicas _funcao = new FuncBasicas();

    @RequestMapping(value = "/Medidas", method = RequestMethod.GET)
    public ResponseEntity<RespostaModel> Medidas(String city) {

        String URL = "/v2/latest?sort=desc&country_id=BR&city=" + city +"&order_by=lastUpdated&dumpRaw=false";

        String resposta = new GerenciaAPI().GET(URL);

        if (resposta.length() == 0) {

            return new ResponseEntity<>(_funcao.MontaModeloResposta("FALHA", "Não foi possível receber resposta da requisição", null), HttpStatus.NOT_FOUND);
        }

        LatestMeasurementResponse lmr = new LatestMeasurementResponse();
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {

            lmr = new Gson().fromJson(resposta, LatestMeasurementResponse.class);

            if (lmr.getResults().isEmpty()) {

                return new ResponseEntity<>(_funcao.MontaModeloResposta("FALHA", "Nenhum registro encontrado", null), HttpStatus.NO_CONTENT);
            }

            MedidaModel medidaModel = new MedidaModel();

            medidaModel.setCidade(lmr.getResults().get(0).getCity());
            medidaModel.setMedidas(lmr.getResults().get(0).getMeasurements());

            return new ResponseEntity<RespostaModel>(_funcao.MontaModeloResposta("OK", "", medidaModel), HttpStatus.OK);
        }
        catch (Exception ex) {

            return new ResponseEntity<RespostaModel>(_funcao.MontaModeloResposta("FALHA", ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/Lista", method = RequestMethod.GET)
    public ResponseEntity<?> Lista() {

        String URI = "/v2/cities?sort=asc&country_id=BR&order_by=city";

        String resposta = new GerenciaAPI().GET(URI);

        if (resposta.length() == 0) {

            return new ResponseEntity<>(_funcao.MontaModeloResposta("FALHA", "Não foi possível receber resposta da requisição", null), HttpStatus.NOT_FOUND);
        }

        CitiesResponse cr = new CitiesResponse();
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            cr = new Gson().fromJson(resposta, CitiesResponse.class);

            if (cr.getResults().isEmpty()) {

                return new ResponseEntity<>(_funcao.MontaModeloResposta("FALHA", "Nenhum registro encontrado", null), HttpStatus.NO_CONTENT);
            }

            List<String> cidades = new ArrayList<String>();

            cr.getResults().forEach(result -> cidades.add(result.getCity()));

            return new ResponseEntity<>(_funcao.MontaModeloResposta("OK", "", cidades), HttpStatus.OK);
        }
        catch (Exception ex) {

            return new ResponseEntity<RespostaModel>(_funcao.MontaModeloResposta("FALHA", ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
