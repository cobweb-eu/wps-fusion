package de.tudresden.gis.fusion.data.binding;

import java.util.UUID;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.n52.wps.io.data.IComplexData;

import de.tudresden.gis.fusion.data.geotools.GTFeatureCollection;
import de.tudresden.gis.fusion.data.rdf.IRI;

public class GTFeatureCollectionBinding implements IComplexData {

	private static final long serialVersionUID = 1L;
	
	protected transient GTFeatureCollection collection;
	
	public GTFeatureCollectionBinding(GTFeatureCollection collection){
		this.collection = collection;
	}

	public GTFeatureCollectionBinding(SimpleFeatureCollection collection) {
		String iri = collection.getID() == null ? "collection_" + UUID.randomUUID() : collection.getID();
		this.collection = new GTFeatureCollection(new IRI(iri), collection);
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
