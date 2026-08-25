/**
 * 
 */
import LedBulb from './LedBulb.js';
import Timer from './Timer.js';

/**
 * Constructor
 */
export default class LedTest {

    timeout = 1;

    /**
     * Constructor
     */
    constructor() {
        //
    }


    /**
     * Set (new) timeout
     * 
     * @param {*} timeout 
     * @returns 
     */
    setTimeout(timeout) {
        this.timeout = timeout || this.timeout;

        return this;
    }


    /**
     * Run LED indicators test
     * 
     * See 'https://stackoverflow.com/a/73037142/6487382'
     * 
     * @param {Array} indicators 
     */
    async test(indicators) {

        for (let i = 0;i < indicators.length;i++) {
            await Timer.sleep(this.timeout);
            await LedBulb.test(Timer.ON.status, indicators[i]);
        }

        for (let i = 0;i < indicators.length;i++) {
            await Timer.sleep(this.timeout);
            await LedBulb.test(Timer.OFF.status, indicators[i]);
        }

        return this;
    }

}

// module.exports = LedTest;