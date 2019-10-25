const fs = require('fs');
const rimraf = require("rimraf");
const ncp = require('ncp').ncp;

const {series} = require('gulp');

function copyConfig(cb) {

    function flattenObj(obj, keyChain = [], flattened = {}) {
        for (let key in obj) {
            let value = obj[key];
            let keys = keyChain.concat(key);

            if (value instanceof Object) {
                flattenObj(value, keys, flattened);
            } else {
                flattened[keys.join('.')] = value;
            }
        }

        return flattened;
    }


    const fileContent = fs.readFileSync("config.json");
    const config = flattenObj(JSON.parse(fileContent));

    let properties = "";

    Object.keys(config).forEach(key => {
        properties += `${key}=${config[key]}\n`;
    });

    fs.writeFileSync("./springApp/src/main/resources/config.properties", properties);
    cb();
}

function clean(cb) {
    rimraf('springApp/src/main/resources/public', () => cb());
}

function copyBuildFiles(cb) {
    ncp.limit = 16;

    ncp('public', 'springApp/src/main/resources/public', function (err) {
        if (err) {
            return console.error(err);
        }
        cb();
    });

}

exports.default = series(copyConfig, clean, copyBuildFiles);
