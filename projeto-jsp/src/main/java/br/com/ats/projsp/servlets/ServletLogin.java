package br.com.ats.projsp.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ats.projsp.classes.model.ModelLogin;
import br.com.ats.projsp.dao.DaoModelLoginRepository;

/**
 * O chamado Controller -> s�o as servlets ou nesse caso ServletLoginController
 */
@WebServlet(urlPatterns = {"/principal/ServletLogin","/ServletLogin"})/*Mapeamento URL onde s�o enviados os dados da tela por par�metros*/
public class ServletLogin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String urlParaAutenticacao = "index.jsp";
	
	private DaoModelLoginRepository loginRepository = new DaoModelLoginRepository();

	/**
	 * CONSTRUTOR PADR�O
	 */
    public ServletLogin() {
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String acao = request.getParameter("acao");
		
		/**
		 * Se o logout for diferente de null e diferente de vazio e a a��o for igual ao logout
		 * a sess�o � finalizada excluindo os dados do usu�rio logado e direcionando para a p�gina
		 * de autentica��o.
		 */
		if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
			request.getSession().invalidate();//Invalida a sess�o, exclui os dados do usu�rio logado na sess�o
			request.getRequestDispatcher(urlParaAutenticacao).forward(request, response);
		} else {
			
			// chamando este m�todo para n�o deixar a tela em branco
			doPost(request, response);
		}
		
	}

	/**
	 * Recebe os dados enviados por um formul�rio
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
		
			/*Pegando os par�metros vindos da tela*/
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String url = request.getParameter("url");
			
			/**
			 * Validando os dados vindos da tela por par�metro
			 */
			if (login != null && !login.isEmpty() && senha != null && !senha.isEmpty()) {
				/*Setando os par�metros no objeto*/
				ModelLogin modelLogin = new ModelLogin();
				modelLogin.setLogin(login);
				modelLogin.setSenha(senha);
				
				/**
				 * Simula��o de autentica��o
				 */
				if (loginRepository.validarAutenticacao(modelLogin)) {
					
					/*Pegando os atributos de sess�o e manter o usu�rio logaod no sistema
					 * Se quiser, pode deixar apenas o login para a senha n�o ficar na sess�o*/
					request.getSession().setAttribute("usuario", modelLogin.getLogin());
					
					if (url == null || url.equals("null")) {
						url = "/principal/principal.jsp";
					}
					
					/**
					 * redirecionando a p�gina ap�s a autentica��o est� correta
					 * 
					 */
					RequestDispatcher redirecionar = request.getRequestDispatcher(url);
					request.setAttribute("msgSucesso", "Usu�rio Logado com sucesso!");
					redirecionar.forward(request, response);
					
				} else {
					RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
					request.setAttribute("msgErro", "Login e/ou senha incorretos!\nPor favor informe dados v�lidos!");
					redirecionar.forward(request, response);
				}
				
			} else {
				RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
				request.setAttribute("msgErro", "Informe o login e senha corretamente!");
				redirecionar.forward(request, response);
			}
		
		}catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msgPagErro", e.getMessage());
			redirecionar.forward(request, response);
		}
	}

}
