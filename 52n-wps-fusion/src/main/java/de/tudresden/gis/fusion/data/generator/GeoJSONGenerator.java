package de.tudresden.gis.fusion.data.generator;

import java.io.IOException;
import java.io.InputStream;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;

import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.geotools.GTFeatureCollection;

public class GeoJSONGenerator extends org.n52.wps.io.datahandler.generator.GeoJSONGenerator {

	public GeoJSONGenerator(){
		super();
		supportedIDataTypes.clear();
		supportedIDataTypes.add(GTFeatureCollectionBinding.class);
	}
	
	@Override
	public InputStream generateStream(IData data, String mimeType, String schema) throws IOException {
		GTFeatureCollection collection = ((GTFeatureCollectionBinding) data).getPayload();
		GTVectorDataBinding binding52n = new GTVectorDataBinding(collection.getSimpleFeatureCollection());
		return super.generateStream(binding52n, mimeType, schema);
	}
	
}
