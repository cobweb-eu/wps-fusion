package de.tudresden.gis.fusion.data.binding;

import org.n52.wps.io.data.IComplexData;

import de.tudresden.gis.fusion.data.geotools.GTFeatureCollection;

public class GTFeatureCollectionBinding implements IComplexData {

	private static final long serialVersionUID = 1L;
	
	protected transient GTFeatureCollection collection;
	
	public GTFeatureCollectionBinding(GTFeatureCollection collection){
		this.collection = collection;
	}

	@Override
	public GTFeatureCollection getPayload() {
		return collection;
	}

	@Override
	public Class<?> getSupportedClass() {
		return GTFeatureCollection.class;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
