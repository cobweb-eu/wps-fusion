# WPS offering spatial data fusion processes to the COBWEB framework

fusion branch for 52n WPS

## Description (from https://github.com/52North/WPS)

The 52°North Web Processing Service (WPS) enables the deployment of geo-processes on the web in a standardized way. It features a pluggable architecture for processes and data encodings. The implementation is based on the current OpenGIS specification: 05-007r7.

Its focus was the creation of an extensible framework to provide algorithms for generalization on the Web. More information available at the [52°North Geoprocessing Community](http://52north.org/geoprocessing).

## Structure

The following module is added to the WPS framework:
* ``/52n-wps-fusion`` - contains wrapper functions for the JAVA spatial data fusion library (https://github.com/GeoinformationSystems/SpatialDataFusion)

## Installation

The project is built using ``Maven install``. The spatial data for unit tests is not uploaded. To prevent build failures, those tests need to be disabled. The dependencies for the group ``de.tudresden.gis`` can be obtained from https://github.com/GeoinformationSystems/SpatialDataFusion.

For running the service, the compiled project (WAR-file) must be deployed using a servlet container (e.g. Tomcat). The service functionality can be accessed using the OGC WPS standard (http://www.opengeospatial.org/standards/wps).

## Example

The following request is used to calculate distance relations between input features. It requires 3 inputs: (1) the reference dataset (IN_SOURCE), (2) the target dataset (IN_TARGET) and (3) a distance threshold (IN_THRESHOLD). The result is a set of feature relations (OUT_RELATIONS) that contains distance measurements for each pair of features within the threshold distance. It supports two types of relations: the distance relation and the intersect relation. The former assings the distance between features, whereas the latter indicates that two input features overlap.

```
<wps:Execute service="WPS" version="1.0.0"
xmlns:wps="http://www.opengis.net/wps/1.0.0"
xmlns:ows="http://www.opengis.net/ows/1.1"
xmlns:ogc="http://www.opengis.net/ogc"
xmlns:xlink="http://www.w3.org/1999/xlink"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://www.opengis.net/wps/1.0.0
http://schemas.opengis.net/wps/1.0.0/wpsExecute_request.xsd">
<ows:Identifier>de.tudresden.gis.fusion.algorithm.GeometryDistance</ows:Identifier>
   <wps:DataInputs>
     <wps:Input>
       <ows:Identifier>IN_SOURCE</ows:Identifier>
       <wps:Reference
schema="http://schemas.opengis.net/gml/3.1.1/base/feature.xsd"
xlink:href="http://cobweb.gis.geo.tu-dresden.de/wfs?service=wfs&amp;version=1.1.0&amp;request=GetFeature&amp;typename=sampleObs&amp;maxfeatures=1000"
method="GET"/>
     </wps:Input>
     <wps:Input>
       <ows:Identifier>IN_TARGET</ows:Identifier>
       <wps:Reference
schema="http://schemas.opengis.net/gml/3.1.1/base/feature.xsd"
xlink:href="http://cobweb.gis.geo.tu-dresden.de/wfs?service=wfs&amp;version=1.1.0&amp;request=GetFeature&amp;typename=sampleObs&amp;maxfeatures=1000"
method="GET"/>
     </wps:Input>
     <wps:Input>
       <ows:Identifier>IN_THRESHOLD</ows:Identifier>
       <wps:Data>
         <wps:LiteralData dataType="xs:double">0.005</wps:LiteralData>
       </wps:Data>
     </wps:Input>
   </wps:DataInputs>
   <wps:ResponseForm>
   <wps:RawDataOutput mimeType="text/turtle">
       <ows:Identifier>OUT_RELATIONS</ows:Identifier>
     </wps:RawDataOutput>
   </wps:ResponseForm>
</wps:Execute>
```

The result of the example is provided to the client using the RDF Turtle serialization. The structure is as follows:

```
_:FeatureRelation_0bc5341d-5cae-4f6e-98c2-ed1f7613b3b1
	a fusion:featureRelation ;
	relation:target <...#sampleObs.48> ;
	relation:source <...#sampleObs.24> ;
	fusion:relationMeasurement [
		a fusion:relationMeasurement ;
		relation:target fusion:geometryProperty ;
		rdf:value "0.0024680243110686883"^^xsd:decimal ;
		relation:source fusion:geometryProperty ;
		dc:description <http://tu-dresden.de/uw/geo/gis/fusion/operation/spatial#distance> ;
	] .
```