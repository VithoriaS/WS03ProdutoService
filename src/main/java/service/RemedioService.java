package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.RemedioDAO;
import model.Remedio;
import spark.Request;
import spark.Response;


public class RemedioService {

	private RemedioDAO RemedioDAO = new RemedioDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_DESCRICAO = 2;
	private final int FORM_ORDERBY_PRECO = 3;
	
	
	public RemedioService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Remedio(), FORM_ORDERBY_DESCRICAO);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Remedio(), orderBy);
	}

	
	public void makeForm(int tipo, Remedio Remedio, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umRemedio = "";
		if(tipo != FORM_INSERT) {
			umRemedio += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/Remedio/list/1\">Novo Remedio</a></b></font></td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t</table>";
			umRemedio += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/Remedio/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Remedio";
				descricao = "leite, pão, ...";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + Remedio.getID();
				name = "Atualizar Remedio (ID " + Remedio.getID() + ")";
				descricao = Remedio.getDescricao();
				buttonLabel = "Atualizar";
			}
			umRemedio += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umRemedio += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td>&nbsp;Descrição: <input class=\"input--register\" type=\"text\" name=\"descricao\" value=\""+ descricao +"\"></td>";
			umRemedio += "\t\t\t<td>Preco: <input class=\"input--register\" type=\"text\" name=\"preco\" value=\""+ Remedio.getPreco() +"\"></td>";
			umRemedio += "\t\t\t<td>Quantidade: <input class=\"input--register\" type=\"text\" name=\"quantidade\" value=\""+ Remedio.getQuantidade() +"\"></td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ Remedio.getDataFabricacao().toString() + "\"></td>";
			umRemedio += "\t\t\t<td>Data de validade: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\""+ Remedio.getDataValidade().toString() + "\"></td>";
			umRemedio += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t</table>";
			umRemedio += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umRemedio += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Remedio (ID " + Remedio.getID() + ")</b></font></td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td>&nbsp;Descrição: "+ Remedio.getDescricao() +"</td>";
			umRemedio += "\t\t\t<td>Preco: "+ Remedio.getPreco() +"</td>";
			umRemedio += "\t\t\t<td>Quantidade: "+ Remedio.getQuantidade() +"</td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t\t<tr>";
			umRemedio += "\t\t\t<td>&nbsp;Data de fabricação: "+ Remedio.getDataFabricacao().toString() + "</td>";
			umRemedio += "\t\t\t<td>Data de validade: "+ Remedio.getDataValidade().toString() + "</td>";
			umRemedio += "\t\t\t<td>&nbsp;</td>";
			umRemedio += "\t\t</tr>";
			umRemedio += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-Remedio>", umRemedio);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Remedios</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/Remedio/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/Remedio/list/" + FORM_ORDERBY_DESCRICAO + "\"><b>Descrição</b></a></td>\n" +
        		"\t<td><a href=\"/Remedio/list/" + FORM_ORDERBY_PRECO + "\"><b>Preço</b></a></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Remedio> Remedios;
		if (orderBy == FORM_ORDERBY_ID) {                 	Remedios = RemedioDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_DESCRICAO) {		Remedios = RemedioDAO.getOrderByDescricao();
		} else if (orderBy == FORM_ORDERBY_PRECO) {			Remedios = RemedioDAO.getOrderByPreco();
		} else {											Remedios = RemedioDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Remedio p : Remedios) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getID() + "</td>\n" +
            		  "\t<td>" + p.getDescricao() + "</td>\n" +
            		  "\t<td>" + p.getPreco() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/Remedio/" + p.getID() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/Remedio/update/" + p.getID() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteRemedio('" + p.getID() + "', '" + p.getDescricao() + "', '" + p.getPreco() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-Remedio>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
		LocalDate dataValidade = LocalDate.parse(request.queryParams("dataValidade"));
		
		String resp = "";
		
		Remedio Remedio = new Remedio(-1, descricao, preco, quantidade, dataFabricacao, dataValidade);
		
		if(RemedioDAO.insert(Remedio) == true) {
            resp = "Remedio (" + descricao + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Remedio (" + descricao + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Remedio Remedio = (Remedio) RemedioDAO.get(id);
		
		if (Remedio != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, Remedio, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Remedio " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Remedio Remedio = (Remedio) RemedioDAO.get(id);
		
		if (Remedio != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, Remedio, FORM_ORDERBY_DESCRICAO);
        } else {
            response.status(404); // 404 Not found
            String resp = "Remedio " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Remedio Remedio = RemedioDAO.get(id);
        String resp = "";       

        if (Remedio != null) {
        	Remedio.setDescricao(request.queryParams("descricao"));
        	Remedio.setPreco(Float.parseFloat(request.queryParams("preco")));
        	Remedio.setQuantidade(Integer.parseInt(request.queryParams("quantidade")));
        	Remedio.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
        	Remedio.setDataValidade(LocalDate.parse(request.queryParams("dataValidade")));
        	RemedioDAO.update(Remedio);
        	response.status(200); // success
            resp = "Remedio (ID " + Remedio.getID() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Remedio (ID \" + Remedio.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Remedio Remedio = RemedioDAO.get(id);
        String resp = "";       

        if (Remedio != null) {
            RemedioDAO.delete(id);
            response.status(200); // success
            resp = "Remedio (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Remedio (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}