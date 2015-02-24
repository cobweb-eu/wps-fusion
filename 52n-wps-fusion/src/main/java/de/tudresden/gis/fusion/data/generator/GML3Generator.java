package de.tudresden.gis.fusion.data.generator;

import java.io.OutputStream;

import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.datahandler.generator.GML3BasicGenerator;

import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.geotools.GTFeatureCollection;

public class GML3Generator extends GML3BasicGenerator {
	
	public GML3Generator(){
		super();
		supportedIDataTypes.add(GTFeatureCollectionBinding.class);
	}
	
	@Override
	public void writeToStream(IData coll, OutputStream os) {
		GTFeatureCollection collection = ((GTFeatureCollectionBinding) coll).getPayload();
		GTVectorDataBinding binding52n = new GTVectorDataBinding(collection.getSimpleFeatureCollection());
		super.writeToStream(binding52n, os);
	}

}
