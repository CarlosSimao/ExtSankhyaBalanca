package br.com.sankhya;

import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.QueryExecutor;
import br.com.sankhya.extensions.actionbutton.Registro;
import java.math.BigDecimal;

public class AcaoPesar implements AcaoRotinaJava{

    public float peso = 0;
    public Registro[] registro;
    public int nunota = 0;
    public int seq = 1;
    
    @Override
    public void doAction(ContextoAcao contexto) throws Exception {
        
        final ArduinoSerial arduino = new ArduinoSerial("COM1");
        Thread t = new Thread(){
            @Override
            public void run() {
                
                arduino.initialize();
                while(true){
                    peso = Float.parseFloat(arduino.read());
                }
            }
        };
        t.start();
        
        
        registro = contexto.getLinhas();
        nunota = Integer.parseInt(registro[0].getCampo("NUNOTA").toString());
        
        QueryExecutor query = contexto.getQuery();
        
        query.setParam("NUNOTA", nunota);
        
        query.nativeSelect("SELECT MAX(SEQUENCIA) AS B FROM TGFITE WHERE NUNOTA = {NUNOTA}");
        while(query.next()){
			seq ++;
		}
        
        Registro item = contexto.novaLinha("TGFITE");
        
        item.setCampo("NUNOTA", BigDecimal.valueOf(nunota).toString());
        item.setCampo("SEQUENCIA", BigDecimal.valueOf(seq).toString());
        item.setCampo("CODEMP", BigDecimal.valueOf(11).toString());
        item.setCampo("CODPROD", BigDecimal.valueOf(3839).toString());
        item.setCampo("CODLOCALORIG", BigDecimal.ZERO.toString());
        item.setCampo("CONTROLE", "");
        item.setCampo("USOPROD", "T");
        item.setCampo("CODCFO", BigDecimal.ZERO.toString());
        item.setCampo("QTDNEG", BigDecimal.valueOf(peso).toString());
        item.setCampo("QTDENTREGUE", BigDecimal.ZERO.toString());
        item.setCampo("QTDCONFERIDA",BigDecimal.ZERO.toString());
        item.setCampo("VLRUNIT",BigDecimal.ZERO.toString());
        item.setCampo("VLRTOT",BigDecimal.ZERO.toString());
        item.setCampo("VLRCUS",BigDecimal.ZERO.toString());
        item.setCampo("BASEIPI",BigDecimal.ZERO.toString());
        item.setCampo("VLRIPI",BigDecimal.ZERO.toString());
        item.setCampo("BASEICMS",BigDecimal.ZERO.toString());
        item.setCampo("VLRICMS",BigDecimal.ZERO.toString());
        item.setCampo("VLRDESC",BigDecimal.ZERO.toString());
        item.setCampo("BASESUBSTIT",BigDecimal.ZERO.toString());
        item.setCampo("VLRSUBST",BigDecimal.ZERO.toString());
        item.setCampo("PENDENTE","S");
        item.setCampo("CODVOL", BigDecimal.ZERO.toString());
        item.setCampo("ATUALESTOQUE",BigDecimal.ZERO.toString());
        item.setCampo("RESERVA","N");
        item.setCampo("STATUSNOTA","A");
        item.setCampo("CODVEND",BigDecimal.ZERO.toString());
        item.setCampo("CODEXEC",BigDecimal.ZERO.toString());
        item.setCampo("FATURAR","S");
        item.setCampo("VLRREPRED",BigDecimal.ZERO.toString());
        item.setCampo("VLRDESCBONIF",BigDecimal.ZERO.toString());
        item.setCampo("PERCDESC",BigDecimal.ZERO.toString());
        item.setCampo("VLRUNITMOE",BigDecimal.ZERO.toString());
        item.setCampo("VLRDESCMOE",BigDecimal.ZERO.toString());
        item.setCampo("VLRTOTMOE",BigDecimal.ZERO.toString());
        
        item.save();
        
        /*StringBuffer mensagem = new StringBuffer();
        mensagem.append("Pesagem realizada com sucesso!");
        contexto.setMensagemRetorno(mensagem.toString());*/

        t.interrupt();
    }
}
