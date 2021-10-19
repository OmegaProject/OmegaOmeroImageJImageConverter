package edu.umassmed.omega.omero.ij.imageConverter;

import org.scijava.convert.ConvertService;
import org.scijava.service.Service;

import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaLoginCredentials;
import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaServerInformation;
import edu.umassmed.omega.omero.commons.OmeroGateway;
import ij.ImagePlus;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROService;
import omero.client;

public class OmeroImageJImageConverter {

	private static ImageJ imagej;
	private final OMEROService ome;
	private final ConvertService conv;

	public OmeroImageJImageConverter() {
		// Thread.currentThread().setContextClassLoader(IJ.getClassLoader());
		if (OmeroImageJImageConverter.imagej == null) {
			OmeroImageJImageConverter.imagej = new ImageJ();
		}
		final Service serv = OmeroImageJImageConverter.imagej
				.get(OMEROService.class);
		this.ome = (OMEROService) serv;
		this.conv = OmeroImageJImageConverter.imagej.convert();
	}

	public ImagePlus convert(final Long imageID, final OmegaGateway gateway) {
		if (!(gateway instanceof OmeroGateway))
			return null;
		Dataset dataset = null;
		try {
			final OmegaLoginCredentials userInfo = gateway.getGatewayUserInfo();
			final OmegaServerInformation serverInfo = gateway
					.getGatewayServerInfo();
			final client client = new client(serverInfo.getHostName(),
					serverInfo.getPort());
			client.createSession(userInfo.getUserName(),
					userInfo.getPassword());
			// dataset = this.ome.downloadImage(
			// ((OmeroGateway) gateway).getClient(), imageID);
			dataset = this.ome.downloadImage(client, imageID);
			client.closeSession();
		} catch (final Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			return null;
		}
		if (dataset == null)
			return null;
		return this.conv.convert(dataset, ImagePlus.class);
	}
	
	public void showImage(final ImagePlus imgPlus) {
		OmeroImageJImageConverter.imagej.ui().show(imgPlus);
	}
}
