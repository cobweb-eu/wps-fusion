package de.tudresden.gis.fusion.data.parser;

import java.io.InputStream;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;

public class GeoJSONParser extends org.n52.wps.io.datahandler.parser.GeoJSONParser {
	
	private static Logger LOGGER = LoggerFactory.getLogger(GeoJSONParser.class);
	
	public GeoJSONParser(){
		super();
		supportedIDataTypes.clear();
		supportedIDataTypes.add(GTFeatureCollectionBinding.class);
	}
	
	@Override
	public IData parse(InputStream input, String mimeType, String schema) {
		IData features = super.parse(input, mimeType, schema);
		if(features instanceof GTVectorDataBinding && features.getPayload() instanceof SimpleFeatureCollection)
			return new GTFeatureCollectionBinding((SimpleFeatureCollection) features.getPayload());
		
		LOGGER.error("Could not parse inputstream, returning null.");
		return null;
	}

}
