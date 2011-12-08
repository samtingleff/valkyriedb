package com.valkyrie.db.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class FileUtils {

	public static File createTempDir(File parent, String prefix, String suffix) throws IOException {
		File child = File.createTempFile(prefix, suffix, parent);
		child.delete();
		child.mkdir();
		return child;
	}

	public static void rmrf(File dir) throws IOException {
		File[] children = dir.listFiles();
		for (File f : children) {
			if (f.isDirectory()) {
				FileUtils.rmrf(f);
				f.delete();
			} else
				f.delete();
		}
		dir.delete();
	}

	public static void writeTarGz(File localPath, OutputStream dest) throws IOException {
		// http://www.thoughtspark.org/node/53
		BufferedOutputStream bOut = null;
		GzipCompressorOutputStream gzOut = null;
		TarArchiveOutputStream tOut = null;

		try {
			bOut = new BufferedOutputStream(dest);
			gzOut = new GzipCompressorOutputStream(bOut);
			tOut = new TarArchiveOutputStream(gzOut);
			addFileToTarGz(tOut, localPath, "");
		} finally {
			tOut.finish();
			tOut.close();
			gzOut.close();
			bOut.close();
			dest.close();
		}
	}

	private static void addFileToTarGz(TarArchiveOutputStream tOut, File path,
			String base) throws IOException {
		String entryName = base + path.getName();
		TarArchiveEntry tarEntry = new TarArchiveEntry(path, entryName);

		tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
		tOut.putArchiveEntry(tarEntry);

		if (path.isFile()) {
			IOUtils.copy(new FileInputStream(path), tOut);

			tOut.closeArchiveEntry();
		} else {
			tOut.closeArchiveEntry();

			File[] children = path.listFiles();

			if (children != null) {
				for (File child : children) {
					addFileToTarGz(tOut, child, entryName + "/");
				}
			}
		}
	}
}
