//package de.tudresden.gis.fusion.data.parser;
//
//import java.io.InputStream;
//
//import org.camunda.bpm.model.bpmn.Bpmn;
//import org.n52.wps.io.data.IData;
//import org.n52.wps.io.datahandler.parser.AbstractParser;
//
//import de.tudresden.gis.fusion.data.binding.BPMNModelBinding;
//import de.tudresden.gis.fusion.data.complex.BPMNModel;
//import de.tudresden.gis.fusion.data.rdf.IRI;
//
//public class BPMNXMLParser extends AbstractParser {
//	
//	public BPMNXMLParser() {
//		super();
//		supportedIDataTypes.add(BPMNModelBinding.class);
//	}
//
//	@Override
//	public IData parse(InputStream input, String mimeType, String schema) {
//		return new BPMNModelBinding(new BPMNModel(new IRI(this.toString()), Bpmn.readModelFromStream(input)));
//	}
//
//}
