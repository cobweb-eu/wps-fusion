//package de.tudresden.gis.fusion.data.binding;
//
//import org.n52.wps.io.data.IComplexData;
//
//public class BPMNModelBinding implements IComplexData {
//	
//	/**
//	 * default serial id
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	protected transient BPMNModel bpmnModel;
//	
//	public BPMNModelBinding(BPMNModel bpmnModel){
//		this.bpmnModel = bpmnModel;
//	}
//
//	@Override
//	public BPMNModel getPayload() {
//		return bpmnModel;
//	}
//
//	@Override
//	public Class<?> getSupportedClass() {
//		return BPMNModel.class;
//	}
//
//	@Override
//	public void dispose() {
//		// TODO Auto-generated method stub
//	}
//
//}
