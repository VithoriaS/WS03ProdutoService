package dao;

import model.Remedio;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class RemedioDAO extends DAO {	
	public RemedioDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Remedio Remedio) {
		boolean status = false;
		try {
			String sql = "INSERT INTO Remedio (descricao, preco, quantidade, datafabricacao, datavalidade) "
		               + "VALUES ('" + Remedio.getDescricao() + "', "
		               + Remedio.getPreco() + ", " + Remedio.getQuantidade() + ", ?, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(Remedio.getDataFabricacao()));
			st.setDate(2, Date.valueOf(Remedio.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Remedio get(int id) {
		Remedio Remedio = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM Remedio WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	 Remedio = new Remedio(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	                				   rs.getInt("quantidade"), 
	        			               rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			               rs.getDate("datavalidade").toLocalDate());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Remedio;
	}
	
	
	public List<Remedio> get() {
		return get("");
	}

	
	public List<Remedio> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Remedio> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Remedio> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Remedio> get(String orderBy) {
		List<Remedio> Remedios = new ArrayList<Remedio>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM Remedio" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Remedio p = new Remedio(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
	        			                rs.getInt("quantidade"),
	        			                rs.getTimestamp("datafabricacao").toLocalDateTime(),
	        			                rs.getDate("datavalidade").toLocalDate());
	            Remedios.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return Remedios;
	}
	
	
	public boolean update(Remedio Remedio) {
		boolean status = false;
		try {  
			String sql = "UPDATE Remedio SET descricao = '" + Remedio.getDescricao() + "', "
					   + "preco = " + Remedio.getPreco() + ", " 
					   + "quantidade = " + Remedio.getQuantidade() + ","
					   + "datafabricacao = ?, " 
					   + "datavalidade = ? WHERE id = " + Remedio.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(Remedio.getDataFabricacao()));
			st.setDate(2, Date.valueOf(Remedio.getDataValidade()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM Remedio WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}