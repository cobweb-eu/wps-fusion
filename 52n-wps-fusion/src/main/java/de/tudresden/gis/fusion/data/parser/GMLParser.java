package de.tudresden.gis.fusion.data.parser;

import java.io.IOException;
import java.io.InputStream;

import org.geotools.xml.Configuration;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.datahandler.parser.AbstractParser;

import de.tudresden.gis.fusion.data.binding.GTFeatureCollectionBinding;
import de.tudresden.gis.fusion.data.feature.geotools.GTFeatureCollection;
import de.tudresden.gis.fusion.data.feature.geotools.GTIndexedFeatureCollection;

public class GMLParser extends AbstractParser {

	public GMLParser() {
		super();
		supportedIDataTypes.add(GTFeatureCollectionBinding.class);
	}
	
	@Override
	public IData parse(InputStream input, String mimeType, String schema) {
		
		Configuration configuration;
		GTFeatureCollection wfsFC = null;
		String identifier = input.toString();
		
		try {
			if(schema.contains("3.")){			
				configuration = new org.geotools.gml3.GMLConfiguration();
				wfsFC = new GTIndexedFeatureCollection(identifier, input, configuration);
			}
		        
			if(schema.contains("2.")){
				configuration = new org.geotools.gml2.GMLConfiguration();
				wfsFC = new GTIndexedFeatureCollection(identifier, input, configuration);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return new GTFeatureCollectionBinding(wfsFC);

	}

}
