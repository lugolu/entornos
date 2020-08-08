package ar.com.thinksoft.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.Blob;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXParseException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

import ar.com.thinksoft.exception.BusinessException;

public class FileCommonFunctions {

	/**
	 * Metodo que permite redimensionar una imagen, se recomienda la lectura del siguiente material:
	 * <P>
	 * http://www.javafrikitutorials.com/2011/10/tutorial-para-manipular-imagenes-en.html
	 *
	 * @param image
	 *            Imagen a redimensionar
	 * @param width
	 *            Anchura deseada
	 * @param height
	 *            Altura deseada
	 * @param keepAspectRatio
	 *            se utiliza para mantener o no el aspecto
	 *            <ul> <li> true: mantiene el aspecto <li> false: no mantiene el aspecto
	 * @param quality
	 *            se utiliza para elegir la calidad de la imagen
	 *            <ul> <li> 1: Alta <li> 2: media <li> 3: baja
	 * @return Un BufferedImage con la imagen redimensionada
	 * @throws BusinessException
	 */
	public static BufferedImage resizeImage(BufferedImage image, int width, int height, boolean keepAspectRatio, int quality) throws BusinessException {
		try {
			if (keepAspectRatio) {
				double thumbRatio = (double) width / (double) height;
				double imageRatio = (double) image.getWidth() / (double) image.getHeight();

				if (thumbRatio < imageRatio) {
					height = (int) (width / imageRatio);
				} else {
					width = (int) (height * imageRatio);
				}
			}

			BufferedImage tmp = new BufferedImage(width, height, image.getType());
			Graphics2D g = tmp.createGraphics();

			switch (quality) {
			case 1:
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				break;
			case 2:
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				break;
			case 3:
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				break;
			default:
				break;
			}

			g.drawImage(image, 0, 0, width, height, 0, 0, image.getWidth(), image.getHeight(), null);
			g.dispose();

			return tmp;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
		}
	}

	/**
	 *
	 * @param imagen
	 * @param width
	 * @param height
	 * @param prefijo
	 * @return
	 * @throws BusinessException
	 */
	public static String cargarImagenBlob(Blob imagen, int width, int height, String prefijo) throws BusinessException {
		String path = null;
		ImageOutputStream out = null;
		try {
			String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
			pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/"
					: (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");

			File f = new File(pathToFile);
			File[] ficheros = f.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().toLowerCase().endsWith("png");
				}
			});
			for (int x = 0; x < ficheros.length; x++) {
				if (ficheros[x].getName().contains(prefijo)) {
					return "/tmp/" + ficheros[x].getName();
				}
			}
			if (imagen != null) {
				BufferedImage image = ImageIO.read(imagen.getBinaryStream());
				if (image != null) {
					image = resizeImage(image, width, height, false, 1);
					File imageFile = File.createTempFile(prefijo, ".png", new File(pathToFile));
					imageFile.deleteOnExit();
					out = ImageIO.createImageOutputStream(imageFile);
					ImageIO.write(image, "png", out);
					out.flush();
					path = "/tmp/" + imageFile.getName();
				}
			} else {
				path = null;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
		} finally {
			if (out != null) {
				try {out.close();} catch (Exception e) {}
			}
		}
		return path;
	}

	/**
	 *
	 * @param imagen
	 * @param width
	 * @param height
	 * @param prefijo
	 * @return
	 * @throws BusinessException
	 */
	@RemapTest(name="cargarImagenByteSize")
	public static String cargarImagenByte(byte[] imagen, int width, int height, String prefijo) throws BusinessException {
		String path = null;
		ImageOutputStream out = null;
		try {
			String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
			pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/"
					: (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");

			File f = new File(pathToFile);
			File[] ficheros = f.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().toLowerCase().endsWith("png");
				}
			});
			for (int x = 0; x < ficheros.length; x++) {
				if (ficheros[x].getName().contains(prefijo)) {
					return "/tmp/" + ficheros[x].getName();
				}
			}

			if (imagen != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagen));
				if (image != null) {
					image = resizeImage(image, width, height, false, 1);
					File imageFile = File.createTempFile(prefijo, ".png", new File(pathToFile));
					imageFile.deleteOnExit();
					out = ImageIO.createImageOutputStream(imageFile);
					ImageIO.write(image, "png", out);
					out.flush();
					path = "/tmp/" + imageFile.getName();
				}
			} else {
				path = null;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
		} finally {
			if (out != null) {
				try {out.close();} catch (Exception e) {}
			}
		}
		return path;
	}

	/**
	 *
	 * @param imagen
	 * @param prefijo
	 * @return
	 * @throws BusinessException
	 */
	public static String cargarImagenByte(byte[] imagen, String prefijo) throws BusinessException {
		return cargarImagenByte(imagen, prefijo, false);
	}

	/**
	 *
	 * @param imagen
	 * @param prefijo
	 * @param unica
	 * @return
	 * @throws BusinessException
	 */
	@RemapTest(name="cargarImagenByteUnica")
	public static String cargarImagenByte(byte[] imagen, String prefijo, boolean unica) throws BusinessException{
		String path = null;
		ImageOutputStream out = null;
		try {
			String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
			pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/"
					: (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");

			if (unica) {
				File f = new File(pathToFile);
				File[] ficheros = f.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().toLowerCase().endsWith("png");
					}
				});
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].getName().contains(prefijo)) {
						return "/tmp/" + ficheros[x].getName();
					}
				}
			}

			if (imagen != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagen));
				if (image != null) {
					File imageFile = File.createTempFile(prefijo+"_", ".png", new File(pathToFile));
					imageFile.deleteOnExit();
					out = ImageIO.createImageOutputStream(imageFile);
					ImageIO.write(image, "png", out);
					out.flush();
					path = "/tmp/" + imageFile.getName();
				}
			} else {
				path = null;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
		} finally {
			if (out != null) {
				try {out.close();} catch (Exception e) {}
			}
		}
		return path;
	}

	/**
	 *
	 * @param imagen
	 * @param prefijo
	 * @param unica
	 * @return
	 * @throws BusinessException
	 */
	public static String cargarBmpImagenByte(byte[] imagen, String prefijo, boolean unica) throws BusinessException{
		String path = null;
		ImageOutputStream out = null;
		try {
			String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
			pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/"
					: (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");

			if (unica) {
				File f = new File(pathToFile);
				File[] ficheros = f.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().toLowerCase().endsWith("bmp");
					}
				});
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].getName().contains(prefijo)) {
						return "/tmp/" + ficheros[x].getName();
					}
				}
			}

			if (imagen != null) {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagen));
				if (image != null) {
					File imageFile = File.createTempFile(prefijo+"_", ".bmp", new File(pathToFile));
					imageFile.deleteOnExit();
					out = ImageIO.createImageOutputStream(imageFile);
					ImageIO.write(image, "bmp", out);
					out.flush();
					path = "/tmp/" + imageFile.getName();
				}
			} else {
				path = null;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
		} finally {
			if (out != null) {
				try {out.close();} catch (Exception e) {}
			}
		}
		return path;
	}

	/**
	 *
	 * @param imagen
	 * @param prefijo
	 * @param unica
	 * @return
	 * @throws BusinessException
	 */
	public static String cargarSvgImagenByte(byte[] imagen, String prefijo, boolean unica) throws BusinessException {
		String path = null;
		ImageOutputStream out = null;
		try {
			String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
			pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/"
					: (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");

			if (unica) {
				File f = new File(pathToFile);
				File[] ficheros = f.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						return file.isDirectory() || file.getName().toLowerCase().endsWith("svg");
					}
				});
				for (int x = 0; x < ficheros.length; x++) {
					if (ficheros[x].getName().contains(prefijo)) {
						return "/tmp/" + ficheros[x].getName();
					}
				}
			}

			if (imagen != null) {
				File imageFile = File.createTempFile(prefijo+"_", ".svg", new File(pathToFile));
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				org.w3c.dom.Document document = builder.parse(new ByteArrayInputStream(imagen));
				SVGTranscoder t = new SVGTranscoder();
				TranscoderInput input = new TranscoderInput(document);
				Writer writer = new FileWriter(imageFile);
				TranscoderOutput output = new TranscoderOutput(writer);
				t.transcode(input, output);
				writer.flush();
				writer.close();
				path = "/tmp/" + imageFile.getName();
			}
			else {
				path = null;
			}
		}
		catch (Exception e) {
			if(e instanceof SAXParseException){
				throw new BusinessException(MessageBundle.SVG_MAL_FORMADO, ((BusinessException) e).getSeverity());
			}
			else{
				throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
			}
		}
		finally {
			if (out != null) {
				try {out.close();} catch (Exception e) {}
			}
		}
		return path;
	}

	/**
	 *
	 * @param files
	 * @param output
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void mergePdfs (List<String> files, File output) throws DocumentException, IOException {
		//		String[] files = { "File1.pdf", "File2.pdf" };
		Document pdfCombineUsingJava = new Document();
		PdfCopy copy = new PdfCopy(pdfCombineUsingJava, new FileOutputStream(output));
		pdfCombineUsingJava.open();
		PdfReader ReadInputPDF;
		int number_of_pages;
		for(String s : files) {
			ReadInputPDF = new PdfReader(s);
			PdfReader.unethicalreading = true;
			number_of_pages = ReadInputPDF.getNumberOfPages();
			for (int page = 0; page < number_of_pages; ) {
				copy.addPage(copy.getImportedPage(ReadInputPDF, ++page));
			}
		}
		pdfCombineUsingJava.close();
	}

	/**
	 *
	 * @param image
	 * @param output
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws BusinessException
	 */
	public static File image2Pdf(Image image, File output) throws DocumentException, IOException, BusinessException {
		try{
			//se escala la imagen para que quepa en la hoja
			image.scaleToFit(PageSize.A4.getWidth(),PageSize.A4.getHeight());
			//se rota la hoja para que quede apaisada
			com.itextpdf.text.Document pdfImg = new com.itextpdf.text.Document(PageSize.A4.rotate());
			OutputStream salida = new FileOutputStream(output.getAbsolutePath());
			com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(pdfImg, salida);
			writer.open();
			pdfImg.open();
			pdfImg.add(com.itextpdf.text.Image.getInstance(image));
			pdfImg.close();
			writer.close();
		}catch (Exception e) {
			throw new BusinessException(e.getMessage(), ((BusinessException) e).getSeverity());
		}
		return output;
	}

	/**
	 *
	 * @param nombre
	 * @param extension
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static File createTempFile (String nombre, String extension, byte[] bytes) throws IOException {
		String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
		pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/" : (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");

		File file = File.createTempFile (nombre + ".", extension, new File(pathToFile));
		if (bytes != null) {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
			fos.close();
		}
		file.deleteOnExit();

		return file;
	}

	/**
	 *
	 * @param contenido
	 * @param extension
	 * @param prefijo
	 * @param unica
	 * @return
	 * @throws IOException
	 */
	public static String bytesToFilePath(byte[] contenido, String extension, String prefijo, boolean unica) throws IOException {
		return bytesToFilePath(contenido, extension, prefijo, unica, false);
	}

	/**
	 *
	 * @param contenido
	 * @param extension
	 * @param prefijo
	 * @param unica
	 * @param fullPath
	 * @return
	 * @throws IOException
	 */
	@RemapTest(name="bytesToFilePathFullPath")
	public static String bytesToFilePath(byte[] contenido, String extension, String prefijo, boolean unica, boolean fullPath) throws IOException {
		String pathToFile = FileCommonFunctions.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
		pathToFile = System.getProperty("file.separator").equals("/") ? "/" + pathToFile.split("WEB-INF")[0] + "/tmp/" : (pathToFile.split("WEB-INF")[0] + "\\tmp\\").replace("/", "\\");
		final String lowerExtension = extension.toLowerCase();

		if (unica) {
			File f = new File(pathToFile);
			File[] ficheros = f.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isDirectory() || file.getName().toLowerCase().endsWith(lowerExtension);
				}
			});
			for (int x = 0; x < ficheros.length; x++) {
				if (ficheros[x].getName().contains(prefijo)) {
					if (fullPath) {
						return ficheros[x].getAbsolutePath();
					}
					else {
						return "/tmp/" + ficheros[x].getName();
					}
				}
			}
		}

		String path = null;
		if (contenido != null) {
			File imageFile = null;
			if(unica){
				// ES A PROPOSITO QUE NO SEA TMP
				imageFile = new File(pathToFile + prefijo + "." + lowerExtension);
				imageFile.deleteOnExit();
			}
			else{
				imageFile = File.createTempFile (prefijo,"." + lowerExtension, new File(pathToFile));
				imageFile.deleteOnExit();
			}

			FileUtils.writeByteArrayToFile(imageFile, contenido);
			if (fullPath) {
				path = imageFile.getAbsolutePath();
			}
			else {
				path = "/tmp/" + imageFile.getName();
			}
		}
		else {
			path = null;
		}
		return path;
	}

}
