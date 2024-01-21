import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-lang-select',
  templateUrl: './lang-select.component.html',
  styleUrls: ['./lang-select.component.scss']
})
export class LangSelectComponent {

  private langMap: Map<string, string> = new Map<string, string>([
    ["en", "English"],
    ["de", "German"]
  ]);

  constructor(private translateService : TranslateService, private globals: Globals) {
  }

  getLangs() : string[] {
    return this.translateService.getLangs();
  }

  setLang(lang : string) : void {
    this.translateService.use(lang);
    console.log("language: " + lang);
    this.globals.currentLanguage = lang;
  }

  formattedLang(lang : string) : string {
    if (this.langMap.has(lang)) {
      return this.langMap.get(lang);
    }
    return "undefined";
  }
}
