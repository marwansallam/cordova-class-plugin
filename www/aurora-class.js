var exec = require("cordova/exec");

/**
 * Constructor.
 *
 * @returns {AuroraClass}
 */
function AuroraClass() { }

AuroraClass.prototype.getLaunchParams = function (successCallback, errorCallback) {

    exec(successCallback, errorCallback, 'Glass', 'get_launch_params', []);
};

module.exports = new AuroraClass();
