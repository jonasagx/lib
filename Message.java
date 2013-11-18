
/*
 * Classe de uso geral, serve para requisitar pedidos e para responder pedidos.
 * 
 * Para requisitar informações de aluno:
 * 	Request: envia-se um objeto Message onde o atributo aluno tenha apenas o ID ou o nome;
 *  Response: como resposta envia-se outro objeto Message onde o atributo aluno possui agora ID, nome e dep
 *
 */

public class Message {
	private Student student = null;
}
