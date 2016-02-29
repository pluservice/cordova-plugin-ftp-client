package org.apache.cordova;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FtpClient extends CordovaPlugin {
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		// Execute
		try {
			// Test
			if ((args == null) || (args.length() < 4)) {
				throw new IllegalArgumentException("Parametri errati");
			}

			// Parameters
			URL Url = new URL(args.getString(0));

			String RemoteFile = args.getString(1);
			if (RemoteFile.endsWith(";type=i") || RemoteFile.endsWith(";type=a")) {
				RemoteFile = RemoteFile.substring(0, RemoteFile.length() - 7);
			}

			String LocalFile = args.getString(2).replace("file://", "");

			JSONObject Credentials = args.getJSONObject(3);

			// Client
			FTPClient Client = new FTPClient();

			// Port
			int Port = (
				(Url.getPort() == -1)
				?	Url.getDefaultPort()
				:	Url.getPort()
			);

			// Connect
			Client.connect(Url.getHost(), Port);

			// Login
			Client.login(
				Credentials.getString("user"),
				Credentials.getString("password")
			);

			// Setup
			Client.enterLocalPassiveMode();
			Client.setFileType(FTP.BINARY_FILE_TYPE);

			// Actions
			if (action.equals("get")) {
				get(Client, Url, RemoteFile, LocalFile);
			}

			if (action.equals("put")) {
				put(Client, Url, RemoteFile, LocalFile);
			}

			// Client
			Client.logout();
			Client.disconnect();

			// Ok
			callbackContext.sendPluginResult(
				new PluginResult(
					PluginResult.Status.OK,
					"file://" + LocalFile
				)
			);
		}
		catch (JSONException E) {
			// Error
			callbackContext.sendPluginResult(
				new PluginResult(
					PluginResult.Status.JSON_EXCEPTION,
					E.getMessage()
				)
			);
		}
		catch (MalformedURLException E) {
			// Error
			callbackContext.sendPluginResult(
				new PluginResult(
					PluginResult.Status.MALFORMED_URL_EXCEPTION,
					E.getMessage()
				)
			);
		}
		catch (IOException E) {
			// Error
			callbackContext.sendPluginResult(
				new PluginResult(
					PluginResult.Status.IO_EXCEPTION,
					E.getMessage()
				)
			);
		}
		catch (Exception E) {
			// Error
			callbackContext.sendPluginResult(
				new PluginResult(
					PluginResult.Status.ERROR,
					E.getMessage()
				)
			);
		}

		// Return
		return true;
	}

	private String change(FTPClient Client, String RemoteFile) throws IOException {
		// Split
		String[] items = RemoteFile.split(Pattern.quote(System.getProperty("file.separator")));

		// Change directories
		if (items.length > 1) {
			for (int index = 0; index < (items.length - 1); index++) {
				if (!items[index].trim().equals("")) {
					Client.changeWorkingDirectory(items[index]);
				}
			}
		}

		// Return
		return items[items.length - 1];
	}
	private void create(String LocalFile) throws IOException {
		// Split
		String[] items = LocalFile.split(Pattern.quote(System.getProperty("file.separator")));

		// Create directories
		if (items.length > 1) {
			String path = "";
			for (int index = 0; index < (items.length - 1); index++) {
				if (!items[index].trim().equals("")) {
					path +=
						(path.equals("") ? "" : System.getProperty("file.separator")) +
						items[index];
				}
			}

			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	private void get(FTPClient Client, URL Url, String RemoteFile, String LocalFile) throws IOException {
		// Change
		RemoteFile = change(Client, RemoteFile);

		// Create
		create(LocalFile);

		// Read
		BufferedOutputStream buffer =
			new BufferedOutputStream(
				new FileOutputStream(LocalFile)
			);

		Client.retrieveFile(RemoteFile, buffer);

		buffer.flush();
		buffer.close();
	}

	private void put(FTPClient Client, URL Url, String RemoteFile, String LocalFile) throws IOException {
		// Change
		RemoteFile = change(Client, RemoteFile);

		// Write
		BufferedInputStream buffer =
			new BufferedInputStream(
				new FileInputStream(LocalFile)
			);

		Client.storeFile(RemoteFile, buffer);

		buffer.close();
	}
}