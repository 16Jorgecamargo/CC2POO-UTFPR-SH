package emp.services;

import emp.models.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;
import emp.dao.ClienteDAO;

/**
 * Serviço para gerenciamento de clientes.
 * Opera sobre uma lista observável e encapsula operações do ClienteDAO.
 */
public class ClienteService {

    private static final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private static final ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Retorna a lista observável de clientes.
     *
     * @return ObservableList com os clientes.
     */
    public static ObservableList<Cliente> getClientes() {
        return clientes;
    }

    /**
     * Atualiza a lista de clientes consultando os dados do banco.
     *
     * @throws SQLException se ocorrer erro na consulta.
     */
    public static void atualizarClientes() throws SQLException {
        List<Cliente> clientesDoBanco = clienteDAO.buscarTodos();
        clientes.setAll(clientesDoBanco);
    }
    
    /**
     * Adiciona um novo cliente à lista.
     *
     * @param cliente novo cliente a ser adicionado.
     */
    public static void addCliente(Cliente cliente) {
        clientes.add(cliente);
    }
    
    /**
     * Remove um cliente da lista.
     *
     * @param cliente cliente a ser removido.
     */
    public static void removeCliente(Cliente cliente) {
        clientes.remove(cliente);
    }
}