cordova.define(
	"cordova-plugin-ftp-client.FtpClient",
	function(require, exports, module) {
		var exec = require("cordova/exec");

		function FtpClient() {
		}

		FtpClient.prototype.get = function (options, successCallback, errorCallback) {
			return exec(
				successCallback,
				errorCallback,
				"FtpClient",
				"get",
				[
					options.url,
					options.remote,
					options.local,
					options.credentials
				]
			);
		};

		FtpClient.prototype.put = function (options, successCallback, errorCallback) {
			return exec(
				successCallback,
				errorCallback,
				"FtpClient",
				"put",
				[
					options.url,
					options.remote,
					options.local,
					options.credentials
				]
			);
		};

		module.exports = new FtpClient();
	}
);
