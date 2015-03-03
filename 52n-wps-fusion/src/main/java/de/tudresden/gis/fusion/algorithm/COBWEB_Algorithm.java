package de.tudresden.gis.fusion.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.n52.wps.server.AbstractAnnotatedAlgorithm;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import de.tudresden.gis.fusion.data.IFeatureRelation;
import de.tudresden.gis.fusion.data.IFeatureRelationCollection;
import de.tudresden.gis.fusion.data.IRelationMeasurement;
import de.tudresden.gis.fusion.data.complex.FeatureRelation;
import de.tudresden.gis.fusion.metadata.data.RelationMeasurementDescription;

public abstract class COBWEB_Algorithm extends AbstractAnnotatedAlgorithm {
	
	private final String TARGET_NO_RELATION = "no_relation";

	/**
     * add feature relation to collection of features
     * @param inTarget target features
     * @param relations relations
     * @return target features with relations
     */
    protected SimpleFeatureCollection addRelations(SimpleFeatureCollection inReference, IFeatureRelationCollection relations, String newAtt){
    	//init feature list
    	List<SimpleFeature> outTargetList = new ArrayList<SimpleFeature>();
    	//get new feature type
    	SimpleFeatureType fType = addAttribute(inReference.getSchema(), newAtt, String.class);
    	//iterate collection and add relation property
    	SimpleFeatureIterator iterator = inReference.features();
    	while (iterator.hasNext()) {
    		SimpleFeature feature = iterator.next();
        	//get relation string
        	String sRelation = getRelationString(feature, relations);
    		//build new feature
    		SimpleFeature newFeature = buildFeature(feature, fType, sRelation, newAtt);
    		//add feature to new collection
    		outTargetList.add(newFeature);
		}
    	//return
    	return DataUtilities.collection(outTargetList);
    }
    
    /**
     * get relation attribute string
     * @param feature input feature
     * @param relations input relations
     * @return relation string for feature
     */
    private String getRelationString(SimpleFeature feature, IFeatureRelationCollection relations){
    	//get feature id
    	String sID = feature.getID();
    	//get relations for reference id
    	List<IFeatureRelation> featureRelations = new ArrayList<IFeatureRelation>();
    	for(IFeatureRelation relation : relations){
    		if(relation.getReference().getFeatureId().endsWith(sID))
    			featureRelations.add(relation);
    	}
    	//identify suitable relation (if array > 1)
    	if(featureRelations.size() == 0)
    		return TARGET_NO_RELATION;
    	else {
    		StringBuilder sRelation = new StringBuilder();
    		for(IFeatureRelation relation : featureRelations){
    			sRelation.append(getRelationString((FeatureRelation) relation) + "&&");
    		}
    		return sRelation.substring(0, sRelation.length()-2);
    	}
    }
    
    /**
     * build new feature
     * @param feature input feature
     * @param fType new feature type
     * @param relation input relation
     * @return new feature with attached relation attribute
     */
    private SimpleFeature buildFeature(SimpleFeature feature, SimpleFeatureType fType, String sRelation, String newAtt){
    	//get feature builder
    	SimpleFeatureBuilder fBuilder= new SimpleFeatureBuilder(fType);
    	//copy feature
    	fBuilder.init(feature);
    	//add relation
    	fBuilder.set(newAtt, sRelation);
		//return new feature
		return fBuilder.buildFeature(feature.getID());
    }
    
    /**
     * get string representation of relation
     * @param relation input relation
     * @return string representation of relation
     */
    private String getRelationString(IFeatureRelation relation){
    	if(relation == null)
    		return TARGET_NO_RELATION;
    	StringBuilder builder = new StringBuilder();
    	Collection<IRelationMeasurement> measurements = relation.getMeasurements();
    	builder.append(relativizeId(relation.getTarget().getFeatureId()) + ":");
    	for(IRelationMeasurement measurement : measurements){
	    	String measurementId = relativizeId(((RelationMeasurementDescription) measurement.getDescription()).getIdentifier().toString());
	    	String measurementValue = measurement.getMeasurementValue().getValue().toString();
	    	builder.append(measurementId + "," + measurementValue + ";");
    	}
    	return builder.substring(0, builder.length() - 1);
    }
    
    /**
     * relativize measurement id, removes namespace
     * @param id input identifier
     * @return relativized identifier
     */
    private String relativizeId(String id){
    	if(id.contains("#"))
    		return id.substring(id.lastIndexOf("#") + 1, id.length());
    	else if(id.contains("/"))
    		return id.substring(id.lastIndexOf("/") + 1, id.length());
    	else
    		return id;
    }
    
    /**
     * build new feature type with additional attribute
     * @param inputType input feature type
     * @param newAtt attribute to be added
     * @return new feature type
     */
    private SimpleFeatureType addAttribute(SimpleFeatureType inputType, String name, Class<?> clazz){
    	//get ft builder
		SimpleFeatureTypeBuilder ftBuilder= new SimpleFeatureTypeBuilder();
		//copy input type
		ftBuilder.init(inputType);
		//set name and default geometry name
		ftBuilder.setName(inputType.getName());
		ftBuilder.setDefaultGeometry(inputType.getGeometryDescriptor().getLocalName());
		//add new attribute property
		ftBuilder.add(name, clazz);
		//return
		return ftBuilder.buildFeatureType();
    }
	
}
