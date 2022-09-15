package app;

import static spark.Spark.*;
import service.RemedioService;


public class Aplicacao {
	
	private static RemedioService remedioService = new RemedioService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/remedio/insert", (request, response) -> remedioService.insert(request, response));

        get("/remedio/:id", (request, response) -> remedioService.get(request, response));
        
        get("/remedio/list/:orderby", (request, response) -> remedioService.getAll(request, response));

        get("/remedio/update/:id", (request, response) -> remedioService.getToUpdate(request, response));
        
        post("/remedio/update/:id", (request, response) -> remedioService.update(request, response));
           
        get("/remedio/delete/:id", (request, response) -> remedioService.delete(request, response));

             
    }
}