var exec = require("cordova/exec");

function FtpClient() {
}

FtpClient.prototype.delete = function (options, successCallback, errorCallback) {
	return exec(
		successCallback,
		errorCallback,
		"FtpClient",
		"delete",
		[
			options.url,
			options.remote,
			options.local,
			options.credentials
		]
	);
};

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

FtpClient.prototype.list = function (options, successCallback, errorCallback) {
	return exec(
		successCallback,
		errorCallback,
		"FtpClient",
		"list",
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
