import com.br.hermescomercialnetbeans.model.Produto;

public class debug_produto_toString {
    public static void main(String[] args) {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Notebook Dell");
        produto.setCodigo("NOTE001");
        
        System.out.println("toString output: " + produto.toString());
    }
}
