//package de.tudresden.gis.fusion.algorithm;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.n52.wps.algorithm.annotation.Algorithm;
//import org.n52.wps.algorithm.annotation.ComplexDataInput;
//import org.n52.wps.algorithm.annotation.Execute;
//import org.n52.wps.algorithm.annotation.LiteralDataOutput;
//import org.n52.wps.io.data.binding.literal.LiteralStringBinding;
//import org.n52.wps.server.AbstractAnnotatedAlgorithm;
//
//import de.tudresden.gis.fusion.data.IData;
//import de.tudresden.gis.fusion.data.binding.BPMNModelBinding;
//import de.tudresden.gis.fusion.data.complex.BPMNModel;
//import de.tudresden.gis.fusion.data.simple.StringLiteral;
//
//@Algorithm(abstrakt="Executes BPMN XML model for local relation processes", version="1.0")
//public class BPMNAggregate extends AbstractAnnotatedAlgorithm {
//	
//	//io identifier
//	private final String IN_BPMN = "IN_BPMN";
//	private final String OUT_RESULT = "OUT_RESULT";
//	
//	private BPMNModel bpmnModel;
//	private String result;
//	
//	@ComplexDataInput(identifier=IN_BPMN, title="BPMN model", binding=BPMNModelBinding.class, minOccurs=1, maxOccurs=1)
//    public void setBPMN(BPMNModel bpmnModel) {
//        this.bpmnModel = bpmnModel;
//    }
//	
//	@LiteralDataOutput(identifier=OUT_RESULT, title="process result" , binding=LiteralStringBinding.class)
//    public String getResult() {
//    	return result;
//    }
//	
//	@Execute
//    public void execute() {
//		//init aggregation process
//		de.tudresden.gis.fusion.operation.aggregate.BPMNAggregate process = new de.tudresden.gis.fusion.operation.aggregate.BPMNAggregate();
//		//set input
//		Map<String,IData> input = new HashMap<String,IData>();
//		input.put("IN_BPMN", bpmnModel);
//		//execute
//		Map<String,IData> output = process.execute(input);
//		//set result
//		StringLiteral result = (StringLiteral) output.get(OUT_RESULT);
//		this.result = result.getValue();
//	}
//
//}
