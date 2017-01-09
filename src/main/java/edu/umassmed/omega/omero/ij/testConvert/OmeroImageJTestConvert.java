package edu.umassmed.omega.omero.ij.testConvert;

import ij.ImagePlus;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.omero.OMEROService;
import omero.ServerError;

import org.scijava.convert.ConvertService;
import org.scijava.service.Service;

import edu.umassmed.omega.commons.data.imageDBConnectionElements.OmegaGateway;
import edu.umassmed.omega.omero.commons.OmeroGateway;

public class OmeroImageJTestConvert {

	private static ImageJ imagej;
	private final OMEROService ome;
	private final ConvertService conv;

	public OmeroImageJTestConvert() {
		// Thread.currentThread().setContextClassLoader(IJ.getClassLoader());
		if (OmeroImageJTestConvert.imagej == null) {
			OmeroImageJTestConvert.imagej = new ImageJ();
		}
		final Service serv = OmeroImageJTestConvert.imagej
				.get(OMEROService.class);
		this.ome = (OMEROService) serv;
		this.conv = OmeroImageJTestConvert.imagej.convert();
	}

	public ImagePlus convert(final Long imageID, final OmegaGateway gateway) {
		if (!(gateway instanceof OmeroGateway))
			return null;
		Dataset dataset = null;
		try {
			dataset = this.ome.downloadImage(
			        ((OmeroGateway) gateway).getClient(), imageID);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (final ServerError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dataset == null)
			return null;
		return this.conv.convert(dataset, ImagePlus.class);
	}
}
