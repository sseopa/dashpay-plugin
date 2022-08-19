var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { WebPlugin } from '@capacitor/core';
export class DashPayModuleWeb extends WebPlugin {
    constructor() {
        super({
            name: 'DashPayModule',
            platforms: ['web'],
        });
    }
    echo(options) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log('ECHO', options);
            return options;
        });
    }
    getSerial(options) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log('getSerial', options);
            return options;
        });
    }
    print(options) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log('print string: ', options.printString);
            console.log('EXTRA_ORIGINATING_URI: ', options.EXTRA_ORIGINATING_URI);
            return {
                results: [{
                        firstName: 'Dummy',
                        lastName: 'Entry',
                        telephone: '123456'
                    }]
            };
        });
    }
    pay(options) {
        return __awaiter(this, void 0, void 0, function* () {
            //console.logoptions('print string: ', options.printString);
            //console.log('EXTRA_ORIGINATING_URI: ', options.EXTRA_ORIGINATING_URI);
            //version: 1.3
            return options;
        });
    }
}
const DashPayModule = new DashPayModuleWeb();
export { DashPayModule };
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(DashPayModule);
//# sourceMappingURL=web.js.map