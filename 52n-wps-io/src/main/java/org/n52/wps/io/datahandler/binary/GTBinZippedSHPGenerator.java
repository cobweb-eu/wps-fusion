package org.n52.wps.io.datahandler.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.encoding.Base64;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.indexed.IndexedShapefileDataStore;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.IllegalAttributeException;
import org.n52.wps.io.IOHandler;
import org.n52.wps.io.IOUtils;
import org.n52.wps.io.IStreamableGenerator;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.datahandler.xml.AbstractXMLGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Generator to create a zipped shapefile by using GDMS drivers:
 * {@link GeotoolsFeatureCollectionDriver} and {@link ShapefileDriver}
 * 
 * @author victorzinho
 */
public class GTBinZippedSHPGenerator extends AbstractXMLGenerator implements
		IStreamableGenerator {
	/**
	 * @throws IllegalArgumentException
	 *             If the <b>data</b> paramater is not a
	 *             {@link GTVectorDataBinding}
	 * @throws RuntimeException
	 * @see org.n52.wps.io.datahandler.xml.AbstractXMLGenerator#generateXML(org.n52.wps.io.data.IData,
	 *      java.lang.String)
	 */
	@Override
	public Node generateXML(IData data, String schema)
			throws IllegalArgumentException, RuntimeException {
		if (!(data instanceof GTVectorDataBinding)) {
			throw new IllegalArgumentException("Unsupported IData type: "
					+ data.getClass() + ". Expecting: "
					+ GTVectorDataBinding.class);

		}

		try {
			// Create the FeatureCollection data source
			GTVectorDataBinding binding = (GTVectorDataBinding) data;
			FeatureCollection collection = binding.getPayload();

			// Create the text node and return it
			String encoded = toBase64ZippedSHP(collection);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			Document document = factory.newDocumentBuilder().newDocument();
			Text text = document.createTextNode(encoded);
			return text;
		} catch (IOException e) {
			throw new RuntimeException("An error has occurred while zipping "
					+ "and encoding the results", e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("An error has occurred while "
					+ "generating the document builder for the response", e);
		} catch (IllegalAttributeException e) {
			throw new RuntimeException("An error has occurred while "
					+ "transforming the results into a shapefile", e);
		}
	}

	/**
	 * @throws IllegalArgumentException
	 *             If the <b>data</b> parameter is not a
	 *             {@link GTVectorDataBinding}
	 * @throws RuntimeException
	 *             If an error occurs while copying the underlying file into the
	 *             given {@link OutputStream}
	 * @see org.n52.wps.io.IStreamableGenerator#writeToStream(org.n52.wps.io.data.IData,
	 *      java.io.OutputStream)
	 */
	@Override
	public void writeToStream(IData data, OutputStream os)
			throws IllegalArgumentException, RuntimeException {
		try {
			if (!(data instanceof GTVectorDataBinding)) {
				throw new IllegalArgumentException("Unsupported IData type: "
						+ data.getClass() + ". Expecting: "
						+ GTVectorDataBinding.class);
			}

			GTVectorDataBinding binding = (GTVectorDataBinding) data;
			FeatureCollection collection = binding.getPayload();

			String encoded = toBase64ZippedSHP(collection);
			os.write(encoded.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("An error has occurred while "
					+ "transforming the results into a shapefile", e);
		} catch (IllegalAttributeException e) {
			throw new RuntimeException("An error has occurred while "
					+ "transforming the results into a shapefile", e);
		}
	}

	@Override
	public OutputStream generate(IData coll) {
		LargeBufferStream stream = new LargeBufferStream();
		this.writeToStream(coll, stream);
		return stream;
	}

	@Override
	public Class<?>[] getSupportedInternalInputDataType() {
		return new Class<?>[] { GTVectorDataBinding.class };
	}

	@Override
	public String[] getSupportedSchemas() {
		return new String[] {};
	}

	@Override
	public boolean isSupportedEncoding(String encoding) {
		return encoding.equals(IOHandler.ENCODING_BASE64);
	}

	@Override
	public String[] getSupportedFormats() {
		return new String[] { IOHandler.MIME_TYPE_ZIPPED_SHP };
	}

	@Override
	public boolean isSupportedSchema(String schema) {
		return schema == null;
	}

	/**
	 * Transforms the given {@link FeatureCollection} into a zipped SHP file
	 * (.shp, .shx, .dbf, .prj) and returs its Base64 encoding
	 * 
	 * @param collection
	 *            the collection to transform
	 * @return the base64 of the zipped shapefile
	 * @throws IOException
	 *             If an error occurs while creating the SHP file or encoding
	 *             the shapefile
	 * @throws IllegalAttributeException
	 *             If an error occurs while writing the features into the the
	 *             shapefile
	 */
	private String toBase64ZippedSHP(FeatureCollection collection)
			throws IOException, IllegalAttributeException {
		File shp = File.createTempFile("shp", ".shp");
		IndexedShapefileDataStore ds = new IndexedShapefileDataStore(shp
				.toURI().toURL());
		ds.createSchema(collection.getSchema());
		FeatureWriter wr = ds.getFeatureWriter(Transaction.AUTO_COMMIT);
		FeatureIterator features = collection.features();
		while (features.hasNext()) {
			Feature feature = features.next();
			Feature next = wr.next();
			for (int i = 0; i < feature.getNumberOfAttributes(); i++) {
				next.setAttribute(i, feature.getAttribute(i));
			}
		}
		wr.close();
		collection.close(features);

		// Zip the shapefile
		String path = shp.getAbsolutePath();
		String baseName = path.substring(0, path.length() - ".shp".length());
		File shx = new File(baseName + ".shx");
		File dbf = new File(baseName + ".dbf");
		File prj = new File(baseName + ".prj");
		File zipped = IOUtils.zip(shp, shx, dbf, prj);

		// Base64 encoding of the zipped file
		InputStream is = new FileInputStream(zipped);
		if (zipped.length() > Integer.MAX_VALUE) {
			throw new IOException("File is too large to process");
		}
		byte[] bytes = new byte[(int) zipped.length()];
		is.read(bytes);
		is.close();
		zipped.delete();

		return Base64.encode(bytes);
	}
}