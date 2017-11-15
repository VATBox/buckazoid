package com.vatbox.money

import java.time.LocalDate

import com.vatbox.money.Money.ToBigDecimal

import scala.collection.mutable


case class Currency(code: String, name: String, symbol: String, exponent: Int, from: Option[LocalDate], to: Option[LocalDate], replacedBy: Option[Currency {type Key <: Currency.Key}]) {
  type Key <: Currency.Key

  def apply[B: ToBigDecimal](amount: B): Money[Key] = Money(amount, this)

  def to[C <: Currency.Key](unit: Currency {type Key = C}): CurrencyExchange[Key, C] = CurrencyExchange(this, unit)

  Currency.currencies += (code → this)
  Currency.currencies += (symbol → this)


  def canEqual(other: Any): Boolean = other.isInstanceOf[Currency]

  override def equals(other: Any): Boolean = other match {
    case that: Currency ⇒
      (that canEqual this) &&
        code == that.code
    case _ ⇒ false
  }

  override def hashCode(): Int = {
    val state = Seq(code)
    state.map(_.hashCode()).foldLeft(0)((a, b) ⇒ 31 * a + b)
  }
}

object Currency {

  //  type Aux[A0, B0] = Foo[A0] { type B = B0  }
  //  type Aux[K <: Currency.Key] = Currency {type Key <: K}

  trait Key

  case class Evidence[C <: Currency.Key](currency: Currency {type Key = C})

  def apply(code: String, name: String, symbol: String, exponent: Int): Currency {type Key <: Currency.Key} = Currency(code, name, symbol, exponent, None, None, None)

  def apply(code: String)(implicit mc: MoneyContext): Currency {type Key <: Currency.Key} = Currency(code, "", "", mc.currencyDefaultExponent, None, None, None)

  private[money] lazy val currencies: mutable.Map[String, Currency {type Key <: Currency.Key}] = mutable.Map()

  def getCurrencies = currencies.toMap
}


//trait USD extends Currency.Key
//object USD extends Currency("USD", "US Dollar", "$", 2, None, None, None) {
//  //  type Key = USD
//  //  implicit val currencyEvidence: Currency.Evidence[USD] = Currency.Evidence(this)
//}


/**
  * List of currencies taken from: https://en.wikipedia.org/wiki/ISO_4217#cite_note-divby5-9
  */

object AED extends Currency("AED", "United Arab Emirates dirham", "", 2, None, None, None) //  United Arab Emirates
object AFN extends Currency("AFN", "Afghan afghani", "؋", 2, None, None, None) //  Afghanistan
object ALL extends Currency("ALL", "Albanian lek", " Lek", 2, None, None, None) //  Albania
object AMD extends Currency("AMD", "Armenian dram", "", 2, None, None, None) //  Armenia
object ANG extends Currency("ANG", "Netherlands Antillean guilder", " ƒ", 2, None, None, None) // " Curaçao (CW),  Sint Maarten (SX)"
object AOA extends Currency("AOA", "Angolan kwanza", "", 2, None, None, None) //  Angola
object ARS extends Currency("ARS", "Argentine peso", "$", 2, None, None, None) //  Argentina
object AUD extends Currency("AUD", "Australian dollar", "$", 2, None, None, None) // " Australia,  Christmas Island (CX),  Cocos (Keeling) Islands (CC),  Heard Island and McDonald Islands (HM),  Kiribati (KI),  Nauru (NR),  Norfolk Island (NF),  Tuvalu (TV)"
object AWG extends Currency("AWG", "Aruban florin", "ƒ", 2, None, None, None) //  Aruba
object AZN extends Currency("AZN", "Azerbaijani manat", "₼", 2, None, None, None) //  Azerbaijan
object BAM extends Currency("BAM", "Bosnia and Herzegovina convertible mark", " KM", 2, None, None, None) //  Bosnia and Herzegovina
object BBD extends Currency("BBD", "Barbados dollar", "$", 2, None, None, None) //  Barbados
object BDT extends Currency("BDT", "Bangladeshi taka", "", 2, None, None, None) //  Bangladesh
object BGN extends Currency("BGN", "Bulgarian lev", "лв", 2, None, None, None) //  Bulgaria
object BHD extends Currency("BHD", "Bahraini dinar", "", 3, None, None, None) //  Bahrain
object BIF extends Currency("BIF", "Burundian franc", "", 0, None, None, None) //  Burundi
object BMD extends Currency("BMD", "Bermudian dollar", "$", 2, None, None, None) //  Bermuda
object BND extends Currency("BND", "Brunei dollar", "$", 2, None, None, None) //  Brunei
object BOB extends Currency("BOB", "Boliviano", "$b", 2, None, None, None) //  Bolivia
object BOV extends Currency("BOV", "Bolivian Mvdol (funds code)", "", 2, None, None, None) //  Bolivia
object BRL extends Currency("BRL", "Brazilian real", "R$", 2, None, None, None) //  Brazil
object BSD extends Currency("BSD", "Bahamian dollar", "$", 2, None, None, None) //  Bahamas
object BTN extends Currency("BTN", "Bhutanese ngultrum", "", 2, None, None, None) //  Bhutan
object BWP extends Currency("BWP", "Botswana pula", "P", 2, None, None, None) //  Botswana
object BYN extends Currency("BYN", "Belarusian ruble", " Br", 2, None, None, None) //  Belarus
object BZD extends Currency("BZD", "Belize dollar", "BZ$", 2, None, None, None) //  Belize
object CAD extends Currency("CAD", "Canadian dollar", "$", 2, None, None, None) //  Canada
object CDF extends Currency("CDF", "Congolese franc", "", 2, None, None, None) //  Democratic Republic of the Congo
object CHE extends Currency("CHE", "WIR Euro (complementary currency)", "", 2, None, None, None) //   Switzerland
object CHF extends Currency("CHF", "Swiss franc", "CHF", 2, None, None, None) // "  Switzerland,  Liechtenstein (LI)"
object CHW extends Currency("CHW", "WIR Franc (complementary currency)", "", 2, None, None, None) //   Switzerland
object CLF extends Currency("CLF", "Unidad de Fomento (funds code)", "", 4, None, None, None) //  Chile
object CLP extends Currency("CLP", "Chilean peso", "$", 0, None, None, None) //  Chile
object CNY extends Currency("CNY", "Chinese yuan", "¥", 2, None, None, None) //  China
object COP extends Currency("COP", "Colombian peso", "$", 2, None, None, None) //  Colombia
object COU extends Currency("COU", "Unidad de Valor Real (UVR) (funds code)", "", 2, None, None, None) //  Colombia
object CRC extends Currency("CRC", "Costa Rican colon", "₡", 2, None, None, None) //  Costa Rica
object CUC extends Currency("CUC", "Cuban convertible peso", "", 2, None, None, None) //  Cuba
object CUP extends Currency("CUP", "Cuban peso", "₱", 2, None, None, None) //  Cuba
object CVE extends Currency("CVE", "Cape Verde escudo", "", 0, None, None, None) //  Cape Verde
object CZK extends Currency("CZK", "Czech koruna", "Kč", 2, None, None, None) //  Czechia [8]
object DJF extends Currency("DJF", "Djiboutian franc", "", 0, None, None, None) //  Djibouti
object DKK extends Currency("DKK", "Danish krone", "kr", 2, None, None, None) // " Denmark,  Faroe Islands (FO),  Greenland (GL)"
object DOP extends Currency("DOP", "Dominican peso", "RD$", 2, None, None, None) //  Dominican Republic
object DZD extends Currency("DZD", "Algerian dinar", "", 2, None, None, None) //  Algeria
object EGP extends Currency("EGP", "Egyptian pound", "£", 2, None, None, None) //  Egypt
object ERN extends Currency("ERN", "Eritrean nakfa", "", 2, None, None, None) //  Eritrea
object ETB extends Currency("ETB", "Ethiopian birr", "", 2, None, None, None) //  Ethiopia
object EUR extends Currency("EUR", "Euro", "€", 2, None, None, None) // " Andorra (AD),  Austria (AT),  Belgium (BE),  Cyprus (CY),  Estonia (EE),  Finland (FI),  France (FR),  Germany (DE),  Greece (GR),  Guadeloupe (GP),  Ireland (IE),  Italy (IT),  Latvia (LV),  Lithuania (LT),  Luxembourg (LU),  Malta (MT),  Martinique (MQ),  Mayotte (YT),  Monaco (MC),  Montenegro (ME),  Netherlands (NL),  Portugal (PT),  Réunion (RE),  Saint Barthélemy (BL),  Saint Pierre and Miquelon (PM),  San Marino (SM),  Slovakia (SK),  Slovenia (SI),  Spain (ES)"
object FJD extends Currency("FJD", "Fiji dollar", "$", 2, None, None, None) //  Fiji
object FKP extends Currency("FKP", "Falkland Islands pound", " £", 2, None, None, None) //  Falkland Islands (pegged to GBP 1:1)
object GBP extends Currency("GBP", "Pound sterling", "£", 2, None, None, None) // " United Kingdom, the  Isle of Man (IM, see Manx pound),  Jersey (JE, see Jersey pound), and  Guernsey (GG, see Guernsey pound)"
object GEL extends Currency("GEL", "Georgian lari", "", 2, None, None, None) //  Georgia
object GHS extends Currency("GHS", "Ghanaian cedi", "¢", 2, None, None, None) //  Ghana
object GIP extends Currency("GIP", "Gibraltar pound", "£", 2, None, None, None) //  Gibraltar (pegged to GBP 1:1)
object GMD extends Currency("GMD", "Gambian dalasi", "", 2, None, None, None) //  Gambia
object GNF extends Currency("GNF", "Guinean franc", "", 0, None, None, None) //  Guinea
object GTQ extends Currency("GTQ", "Guatemalan quetzal", "Q", 2, None, None, None) //  Guatemala
object GYD extends Currency("GYD", "Guyanese dollar", "$", 2, None, None, None) //  Guyana
object HKD extends Currency("HKD", "Hong Kong dollar", "$", 2, None, None, None) //  Hong Kong
object HNL extends Currency("HNL", "Honduran lempira", "L", 2, None, None, None) //  Honduras
object HRK extends Currency("HRK", "Croatian kuna", "kn", 2, None, None, None) //  Croatia
object HTG extends Currency("HTG", "Haitian gourde", "", 2, None, None, None) //  Haiti
object HUF extends Currency("HUF", "Hungarian forint", "Ft", 2, None, None, None) //  Hungary
object IDR extends Currency("IDR", "Indonesian rupiah", "Rp", 2, None, None, None) //  Indonesia
object ILS extends Currency("ILS", "Israeli new shekel", "₪", 2, None, None, None) //  Israel
object INR extends Currency("INR", "Indian rupee", " ", 2, None, None, None) // " India,  Bhutan"
object IQD extends Currency("IQD", "Iraqi dinar", "", 3, None, None, None) //  Iraq
object IRR extends Currency("IRR", "Iranian rial", "﷼", 2, None, None, None) //  Iran
object ISK extends Currency("ISK", "Icelandic króna", "kr", 0, None, None, None) //  Iceland
object JMD extends Currency("JMD", "Jamaican dollar", "J$", 2, None, None, None) //  Jamaica
object JOD extends Currency("JOD", "Jordanian dinar", "", 3, None, None, None) //  Jordan
object JPY extends Currency("JPY", "Japanese yen", "¥", 0, None, None, None) //  Japan
object KES extends Currency("KES", "Kenyan shilling", "", 2, None, None, None) //  Kenya
object KGS extends Currency("KGS", "Kyrgyzstani som", "лв", 2, None, None, None) //  Kyrgyzstan
object KHR extends Currency("KHR", "Cambodian riel", "៛", 2, None, None, None) //  Cambodia
object KMF extends Currency("KMF", "Comoro franc", "", 0, None, None, None) //  Comoros
object KPW extends Currency("KPW", "North Korean won", "₩", 2, None, None, None) //  North Korea
object KRW extends Currency("KRW", "South Korean won", "₩", 0, None, None, None) //  South Korea
object KWD extends Currency("KWD", "Kuwaiti dinar", "", 3, None, None, None) //  Kuwait
object KYD extends Currency("KYD", "Cayman Islands dollar", "$", 2, None, None, None) //  Cayman Islands
object KZT extends Currency("KZT", "Kazakhstani tenge", "лв", 2, None, None, None) //  Kazakhstan
object LAK extends Currency("LAK", "Lao kip", "₭", 2, None, None, None) //  Laos
object LBP extends Currency("LBP", "Lebanese pound", "£", 2, None, None, None) //  Lebanon
object LKR extends Currency("LKR", "Sri Lankan rupee", "₨", 2, None, None, None) //  Sri Lanka
object LRD extends Currency("LRD", "Liberian dollar", "$", 2, None, None, None) //  Liberia
object LSL extends Currency("LSL", "Lesotho loti", "", 2, None, None, None) //  Lesotho
object LYD extends Currency("LYD", "Libyan dinar", "", 3, None, None, None) //  Libya
object MAD extends Currency("MAD", "Moroccan dirham", "", 2, None, None, None) //  Morocco
object MDL extends Currency("MDL", "Moldovan leu", "", 2, None, None, None) //  Moldova
object MGA extends Currency("MGA", "Malagasy ariary", "", 1, None, None, None) //  Madagascar
object MKD extends Currency("MKD", "Macedonian denar", "ден", 2, None, None, None) //  Macedonia
object MMK extends Currency("MMK", "Myanmar kyat", "", 2, None, None, None) //  Myanmar
object MNT extends Currency("MNT", "Mongolian tögrög", "₮", 2, None, None, None) //  Mongolia
object MOP extends Currency("MOP", "Macanese pataca", "", 2, None, None, None) //  Macao
object MRO extends Currency("MRO", "Mauritanian ouguiya", "", 1, None, None, None) //  Mauritania
object MUR extends Currency("MUR", "Mauritian rupee", "₨", 2, None, None, None) //  Mauritius
object MVR extends Currency("MVR", "Maldivian rufiyaa", "", 2, None, None, None) //  Maldives
object MWK extends Currency("MWK", "Malawian kwacha", "", 2, None, None, None) //  Malawi
object MXN extends Currency("MXN", "Mexican peso", "$", 2, None, None, None) //  Mexico
object MXV extends Currency("MXV", "Mexican Unidad de Inversion (UDI) (funds code)", "", 2, None, None, None) //  Mexico
object MYR extends Currency("MYR", "Malaysian ringgit", "RM", 2, None, None, None) //  Malaysia
object MZN extends Currency("MZN", "Mozambican metical", "MT", 2, None, None, None) //  Mozambique
object NAD extends Currency("NAD", "Namibian dollar", "$", 2, None, None, None) //  Namibia
object NGN extends Currency("NGN", "Nigerian naira", "₦", 2, None, None, None) //  Nigeria
object NIO extends Currency("NIO", "Nicaraguan córdoba", "C$", 2, None, None, None) //  Nicaragua
object NOK extends Currency("NOK", "Norwegian krone", "kr", 2, None, None, None) // " Norway,  Svalbard and  Jan Mayen (SJ),  Bouvet Island (BV)"
object NPR extends Currency("NPR", "Nepalese rupee", "₨", 2, None, None, None) //    Nepal
object NZD extends Currency("NZD", "New Zealand dollar", " $", 2, None, None, None) // " New Zealand,  Cook Islands (CK),  Niue (NU),  Pitcairn Islands (PN; see also Pitcairn Islands dollar),  Tokelau (TK)"
object OMR extends Currency("OMR", "Omani rial", "﷼", 3, None, None, None) //  Oman
object PAB extends Currency("PAB", "Panamanian balboa", "B/.", 2, None, None, None) //  Panama
object PEN extends Currency("PEN", "Peruvian Sol", "S/.", 2, None, None, None) //  Peru
object PGK extends Currency("PGK", "Papua New Guinean kina", "", 2, None, None, None) //  Papua New Guinea
object PHP extends Currency("PHP", "Philippine piso[10]", "₱", 2, None, None, None) //  Philippines
object PKR extends Currency("PKR", "Pakistani rupee", "₨", 2, None, None, None) //  Pakistan
object PLN extends Currency("PLN", "Polish złoty", "zł", 2, None, None, None) //  Poland
object PYG extends Currency("PYG", "Paraguayan guaraní", " Gs", 0, None, None, None) //  Paraguay
object QAR extends Currency("QAR", "Qatari riyal", "﷼", 2, None, None, None) //  Qatar
object RON extends Currency("RON", "Romanian leu", "lei", 2, None, None, None) //  Romania
object RSD extends Currency("RSD", "Serbian dinar", "Дин.", 2, None, None, None) //  Serbia
object RUB extends Currency("RUB", "Russian ruble", "₽", 2, None, None, None) //  Russia
object RWF extends Currency("RWF", "Rwandan franc", "", 0, None, None, None) //  Rwanda
object SAR extends Currency("SAR", "Saudi riyal", "﷼", 2, None, None, None) //  Saudi Arabia
object SBD extends Currency("SBD", "Solomon Islands dollar", "$", 2, None, None, None) //  Solomon Islands
object SCR extends Currency("SCR", "Seychelles rupee", "₨", 2, None, None, None) //  Seychelles
object SDG extends Currency("SDG", "Sudanese pound", "", 2, None, None, None) //  Sudan
object SEK extends Currency("SEK", "Swedish krona/kronor", "kr", 2, None, None, None) //  Sweden
object SGD extends Currency("SGD", "Singapore dollar", "$", 2, None, None, None) //  Singapore
object SHP extends Currency("SHP", "Saint Helena pound", "£", 2, None, None, None) // " Saint Helena (SH-SH),  Ascension Island (SH-AC),  Tristan da Cunha"
object SLL extends Currency("SLL", "Sierra Leonean leone", "", 2, None, None, None) //  Sierra Leone
object SOS extends Currency("SOS", "Somali shilling", "S", 2, None, None, None) //  Somalia
object SRD extends Currency("SRD", "Surinamese dollar", "$", 2, None, None, None) //  Suriname
object SSP extends Currency("SSP", "South Sudanese pound", "", 2, None, None, None) //  South Sudan
object STD extends Currency("STD", "São Tomé and Príncipe dobra", "", 2, None, None, None) //  São Tomé and Príncipe
object SVC extends Currency("SVC", "Salvadoran colón", "$", 2, None, None, None) //  El Salvador
object SYP extends Currency("SYP", "Syrian pound", "£", 2, None, None, None) //  Syria
object SZL extends Currency("SZL", "Swazi lilangeni", "", 2, None, None, None) //  Swaziland
object THB extends Currency("THB", "Thai baht", "฿", 2, None, None, None) //  Thailand
object TJS extends Currency("TJS", "Tajikistani somoni", "", 2, None, None, None) //  Tajikistan
object TMT extends Currency("TMT", "Turkmenistan manat", "", 2, None, None, None) //  Turkmenistan
object TND extends Currency("TND", "Tunisian dinar", "", 3, None, None, None) //  Tunisia
object TOP extends Currency("TOP", "Tongan paʻanga", "", 2, None, None, None) //  Tonga
object TRY extends Currency("TRY", "Turkish lira", " ", 2, None, None, None) //  Turkey
object TTD extends Currency("TTD", "Trinidad and Tobago dollar", "TT$", 2, None, None, None) //  Trinidad and Tobago
object TWD extends Currency("TWD", "New Taiwan dollar", "NT$", 2, None, None, None) //  Taiwan
object TZS extends Currency("TZS", "Tanzanian shilling", "", 2, None, None, None) //  Tanzania
object UAH extends Currency("UAH", "Ukrainian hryvnia", "₴", 2, None, None, None) //  Ukraine
object UGX extends Currency("UGX", "Ugandan shilling", "", 0, None, None, None) //  Uganda
object USD extends Currency("USD", "United States dollar", "$", 2, None, None, None) // " United States,  American Samoa (AS),  Barbados (BB) (as well as Barbados Dollar),  Bermuda (BM) (as well as Bermudian Dollar),  British Indian Ocean Territory (IO) (also uses GBP),  British Virgin Islands (VG),  Caribbean Netherlands (BQ - Bonaire, Sint Eustatius and Saba),  Ecuador (EC),  El Salvador (SV),  Guam (GU),  Haiti (HT),  Marshall Islands (MH),  Federated States of Micronesia (FM),  Northern Mariana Islands (MP),  Palau (PW),  Panama (PA) (as well as Panamanian Balboa),  Puerto Rico (PR),  Timor-Leste (TL),  Turks and Caicos Islands (TC),  U.S. Virgin Islands (VI),  United States Minor Outlying Islands"
object USN extends Currency("USN", "United States dollar (next day) (funds code)", "", 2, None, None, None) //  United States
object UYI extends Currency("UYI", "Uruguay Peso en Unidades Indexadas (URUIURUI) (funds code)", "", 0, None, None, None) //  Uruguay
object UYU extends Currency("UYU", "Uruguayan peso", "$U", 2, None, None, None) //  Uruguay
object UZS extends Currency("UZS", "Uzbekistan som", "лв", 2, None, None, None) //  Uzbekistan
object VEF extends Currency("VEF", "Venezuelan bolívar", "Bs", 2, None, None, None) //  Venezuela
object VND extends Currency("VND", "Vietnamese đồng", "₫", 0, None, None, None) //  Vietnam
object VUV extends Currency("VUV", "Vanuatu vatu", "", 0, None, None, None) //  Vanuatu
object WST extends Currency("WST", "Samoan tala", "", 2, None, None, None) //  Samoa
object XAF extends Currency("XAF", "CFA franc BEAC", "", 0, None, None, None) // " Cameroon (CM),  Central African Republic (CF),  Republic of the Congo (CG),  Chad (TD),  Equatorial Guinea (GQ),  Gabon (GA)"
object XAG extends Currency("XAG", "Silver (one troy ounce)", "", 4, None, None, None) //
object XAU extends Currency("XAU", "Gold (one troy ounce)", "", 4, None, None, None) //
object XBA extends Currency("XBA", "European Composite Unit (EURCO) (bond market unit)", "", 4, None, None, None) //
object XBB extends Currency("XBB", "European Monetary Unit (E.M.U.-6) (bond market unit)", "", 4, None, None, None) //
object XBC extends Currency("XBC", "European Unit of Account 9 (E.U.A.-9) (bond market unit)", "", 4, None, None, None) //
object XBD extends Currency("XBD", "European Unit of Account 17 (E.U.A.-17) (bond market unit)", "", 4, None, None, None) //
object XCD extends Currency("XCD", "East Caribbean dollar", "$", 2, None, None, None) // " Anguilla (AI),  Antigua and Barbuda (AG),  Dominica (DM),  Grenada (GD),  Montserrat (MS),  Saint Kitts and Nevis (KN),  Saint Lucia (LC),  Saint Vincent and the Grenadines (VC)"
object XDR extends Currency("XDR", "Special drawing rights", "", 4, None, None, None) // International Monetary Fund
object XOF extends Currency("XOF", "CFA franc BCEAO", "", 0, None, None, None) // " Benin (BJ),  Burkina Faso (BF),  Côte d'Ivoire (CI),  Guinea-Bissau (GW),  Mali (ML),  Niger (NE),  Senegal (SN),  Togo (TG)"
object XPD extends Currency("XPD", "Palladium (one troy ounce)", "", 4, None, None, None) //
object XPF extends Currency("XPF", "CFP franc (franc Pacifique)", "", 0, None, None, None) // "French territories of the Pacific Ocean:  French Polynesia (PF),  New Caledonia (NC),  Wallis and Futuna (WF)"
object XPT extends Currency("XPT", "Platinum (one troy ounce)", "", 4, None, None, None) //
object XSU extends Currency("XSU", "SUCRE", "", 4, None, None, None) // Unified System for Regional Compensation (SUCRE)[11]
object XTS extends Currency("XTS", "Code reserved for testing purposes", "", 4, None, None, None) //
object XUA extends Currency("XUA", "ADB Unit of Account", "", 4, None, None, None) // African Development Bank[12]
object XXX extends Currency("XXX", "No currency", "", 4, None, None, None) //
object YER extends Currency("YER", "Yemeni rial", "﷼", 2, None, None, None) //  Yemen
object ZAR extends Currency("ZAR", "South African rand", "R", 2, None, None, None) //  South Africa
object ZMW extends Currency("ZMW", "Zambian kwacha", "", 2, None, None, None) //  Zambia
object ZWL extends Currency("ZWL", "Zimbabwean dollar A/10", "", 2, None, None, None) //  Zimbabwe

/**
  * List of Old currencies taken from: https://en.wikipedia.org/wiki/ISO_4217#cite_note-divby5-9
  */
object XFU extends Currency("XFU", "UIC franc (special settlement currency)", "", 0,  None, Some(LocalDate.of(2013,11,7)), Some(EUR))
object ADF extends Currency("ADF", "Andorran franc", "", 2, Some(LocalDate.of(1960,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object ADP extends Currency("ADP", "Andorran peseta", "", 0, Some(LocalDate.of(1869,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object ATS extends Currency("ATS", "Austrian schilling", "", 2, Some(LocalDate.of(1945,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object BAD extends Currency("BAD", "Bosnia and Herzegovina dinar", "", 2, Some(LocalDate.of(1992,7,1)), Some(LocalDate.of(1998,1,1)), Some(BAM))
object BEF extends Currency("BEF", "Belgian franc", "", 2, Some(LocalDate.of(1832,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object BYB extends Currency("BYB", "Belarusian ruble", "", 2, Some(LocalDate.of(1992,1,1)), Some(LocalDate.of(1999,12,31)), Some(BYR))// (BYN))
object BYR extends Currency("BYR", "Belarusian ruble", "", 0, Some(LocalDate.of(2000,1,1)), Some(LocalDate.of(2016,6,30)), Some(BYN))
object CYP extends Currency("CYP", "Cypriot pound", "", 2, Some(LocalDate.of(1879,1,1)), Some(LocalDate.of(2007,12,31)), Some(EUR))
object DEM extends Currency("DEM", "German mark", "", 2, Some(LocalDate.of(1948,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object EEK extends Currency("EEK", "Estonian kroon", "", 2, Some(LocalDate.of(1992,1,1)), Some(LocalDate.of(2010,12,31)), Some(EUR))
object ESP extends Currency("ESP", "Spanish peseta", "", 0, Some(LocalDate.of(1869,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object FIM extends Currency("FIM", "Finnish markka", "", 2, Some(LocalDate.of(1860,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object FRF extends Currency("FRF", "French franc", "", 2, Some(LocalDate.of(1960,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object GRD extends Currency("GRD", "Greek drachma", "", 2, Some(LocalDate.of(1954,1,1)), Some(LocalDate.of(2000,12,31)), Some(EUR))
object IEP extends Currency("IEP", "Irish pound", "", 2, Some(LocalDate.of(1938,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object ITL extends Currency("ITL", "Italian lira", "", 0, Some(LocalDate.of(1861,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object LTL extends Currency("LTL", "Lithuanian litas", "", 2, Some(LocalDate.of(1993,1,1)), Some(LocalDate.of(2014,12,31)), Some(EUR))
object LUF extends Currency("LUF", "Luxembourg franc", "", 2, Some(LocalDate.of(1944,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object LVL extends Currency("LVL", "Latvian lats", "", 2, Some(LocalDate.of(1992,1,1)), Some(LocalDate.of(2013,12,31)), Some(EUR))
object MCF extends Currency("MCF", "Monegasque franc", "", 2, Some(LocalDate.of(1960,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object MAF extends Currency("MAF", "Moroccan franc", "", 0, Some(LocalDate.of(1921,1,1)), Some(LocalDate.of(1976,1,1)), Some(MAD))
object MTL extends Currency("MTL", "Maltese lira", "", 2, Some(LocalDate.of(1972,1,1)), Some(LocalDate.of(2007,12,31)), Some(EUR))
object NLG extends Currency("NLG", "Dutch guilder", "", 2, Some(LocalDate.of(1810,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object PTE extends Currency("PTE", "Portuguese escudo", "", 0, Some(LocalDate.of(1911,5,22)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object SIT extends Currency("SIT", "Slovenian tolar", "", 2, Some(LocalDate.of(1991,10,8)), Some(LocalDate.of(2006,12,31)), Some(EUR))
object SKK extends Currency("SKK", "Slovak koruna", "", 2, Some(LocalDate.of(1993,2,8)), Some(LocalDate.of(2008,12,31)), Some(EUR))
object SML extends Currency("SML", "San Marinese lira", "", 0, Some(LocalDate.of(1864,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object VAL extends Currency("VAL", "Vatican lira", "", 0, Some(LocalDate.of(1929,1,1)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object XEU extends Currency("XEU", "European Currency Unit", "", 0, Some(LocalDate.of(1979,3,13)), Some(LocalDate.of(1998,12,31)), Some(EUR))
object AFA extends Currency("AFA", "Afghan afghani", "", 0, Some(LocalDate.of(1925,1,1)), Some(LocalDate.of(2003,1,1)), Some(AFN))
object AOK extends Currency("AOK", "Angolan kwanza", "", 0, Some(LocalDate.of(1977,1,8)), Some(LocalDate.of(1990,9,24)), Some(AON))
object AON extends Currency("AON", "Angolan new kwanza", "", 0, Some(LocalDate.of(1990,9,25)), Some(LocalDate.of(1995,6,30)), Some(AOR))
object AOR extends Currency("AOR", "Angolan kwanza readjustado", "", 0, Some(LocalDate.of(1995,7,1)), Some(LocalDate.of(1999,11,30)), Some(AOA))
object ARL extends Currency("ARL", "Argentine peso ley", "", 2, Some(LocalDate.of(1970,1,1)), Some(LocalDate.of(1983,5,5)), Some(ARP))
object ARP extends Currency("ARP", "Argentine peso argentino", "", 2, Some(LocalDate.of(1983,6,6)), Some(LocalDate.of(1985,6,14)), Some(ARA))
object ARA extends Currency("ARA", "Argentine austral", "", 2, Some(LocalDate.of(1985,6,15)), Some(LocalDate.of(1991,12,31)), Some(ARS))
object AZM extends Currency("AZM", "Azerbaijani manat", "", 0, Some(LocalDate.of(1992,8,15)), Some(LocalDate.of(2006,1,1)), Some(AZN))
object BGL extends Currency("BGL", "Bulgarian lev A/99", "", 2, Some(LocalDate.of(1962,1,1)), Some(LocalDate.of(1999,7,5)), Some(BGN))
object BOP extends Currency("BOP", "Bolivian peso", "", 2, Some(LocalDate.of(1963,1,1)), Some(LocalDate.of(1987,1,1)), Some(BOB))
object BRB extends Currency("BRB", "Brazilian cruzeiro A/86", "", 2, Some(LocalDate.of(1970,1,1)), Some(LocalDate.of(1986,2,28)), Some(BRC))
object BRC extends Currency("BRC", "Brazilian cruzado A/89", "", 2, Some(LocalDate.of(1986,2,28)), Some(LocalDate.of(1989,1,15)), Some(BRN))
object BRN extends Currency("BRN", "Brazilian cruzado novo A/90", "", 2, Some(LocalDate.of(1989,1,16)), Some(LocalDate.of(1990,3,15)), Some(BRE))
object BRE extends Currency("BRE", "Brazilian cruzeiro A/93", "", 2, Some(LocalDate.of(1990,3,15)), Some(LocalDate.of(1993,8,1)), Some(BRR))
object BRR extends Currency("BRR", "Brazilian cruzeiro real A/94", "", 2, Some(LocalDate.of(1993,8,1)), Some(LocalDate.of(1994,6,30)), Some(BRL))
object CSD extends Currency("CSD", "Serbian dinar", "", 2, Some(LocalDate.of(2003,7,3)), Some(LocalDate.of(2006,1,1)), Some(RSD))
object CSK extends Currency("CSK", "Czechoslovak koruna", "", 0, Some(LocalDate.of(1919,4,10)), Some(LocalDate.of(1993,2,8)), Some(CZK))
object DDM extends Currency("DDM", "East German mark", "", 0, Some(LocalDate.of(1948,6,21)), Some(LocalDate.of(1990,7,1)), Some(DEM))
object ECS extends Currency("ECS", "Ecuadorian sucre", "", 0, Some(LocalDate.of(1884,1,1)), Some(LocalDate.of(2000,1,1)), Some(USD))
object ECV extends Currency("ECV", "Ecuador Unidad de Valor Constante (funds code)", "", 0, Some(LocalDate.of(1993,1,1)), Some(LocalDate.of(2000,1,9)), None)
object GQE extends Currency("GQE", "Equatorial Guinean ekwele", "", 0, Some(LocalDate.of(1975,1,1)), Some(LocalDate.of(1985,1,1)), Some(XAF))
object ESA extends Currency("ESA", "Spanish peseta (account A)", "", 0, Some(LocalDate.of(1978,1,1)), Some(LocalDate.of(1981,1,1)), Some(ESP))
object ESB extends Currency("ESB", "Spanish peseta (account B)", "", 0, Some(LocalDate.of(1994,1,1)) ,Some(LocalDate.of(2012,1,1)), Some(ESP))
object GNE extends Currency("GNE", "Guinean syli", "", 0, Some(LocalDate.of(1971,1,1)), Some(LocalDate.of(1985,1,1)), Some(GNF))
object GHC extends Currency("GHC", "Ghanaian cedi", "", 0, Some(LocalDate.of(1967,1,1)), Some(LocalDate.of(2007,7,1)), Some(GHS))
object GWP extends Currency("GWP", "Guinea-Bissau peso", "", 0, Some(LocalDate.of(1975,1,1)), Some(LocalDate.of(1997,1,1)), Some(XOF))
object HRD extends Currency("HRD", "Croatian dinar", "", 0, Some(LocalDate.of(1991,12,23)), Some(LocalDate.of(1994,5,30)), Some(HRK))
object ILP extends Currency("ILP", "Israeli lira", "", 3, Some(LocalDate.of(1948,1,1)), Some(LocalDate.of(1980,2,23)), Some(ILR))
object ILR extends Currency("ILR", "Israeli shekel", "", 2, Some(LocalDate.of(1980,2,24)), Some(LocalDate.of(1985,12,31)), Some(ILS))
object ISJ extends Currency("ISJ", "Icelandic krona", "", 2, Some(LocalDate.of(1922,1,1)), Some(LocalDate.of(1981,1,1)), Some(ISK))
object LAJ extends Currency("LAJ", "Lao kip", "", 0,  None, Some(LocalDate.of(1979,1,1)), Some(LAK))
object MGF extends Currency("MGF", "Malagasy franc", "", 2, Some(LocalDate.of(1963,7,1)), Some(LocalDate.of(2005,1,1)), Some(MGA))
object MKN extends Currency("MKN", "Old Macedonian denar A/93", "", 0, None, Some(LocalDate.of(1993,1,1)), Some(MKD))
object MLF extends Currency("MLF", "Mali franc", "", 0,  None, Some(LocalDate.of(1984,1,1)), Some(XOF))
object MVQ extends Currency("MVQ", "Maldivian rupee", "", 0, None, Some(LocalDate.of(1981,1,1)), Some(MVR))
object MXP extends Currency("MXP", "Mexican peso", "", 0, None, Some(LocalDate.of(1993,1,1)), Some(MXN))
object MZM extends Currency("MZM", "Mozambican metical", "", 0, Some(LocalDate.of(1980,1,1)), Some(LocalDate.of(2006,6,30)), Some(MZN))
object NIC extends Currency("NIC", "Nicaraguan córdoba", "", 2, Some(LocalDate.of(1988,1,1)), Some(LocalDate.of(1990,1,1)), Some(NIO))
object PEH extends Currency("PEH", "Peruvian old sol", "", 0, Some(LocalDate.of(1863,1,1)), Some(LocalDate.of(1985,2,1)), Some(PEI))
object PEI extends Currency("PEI", "Peruvian inti", "", 0, Some(LocalDate.of(1985,2,1)), Some(LocalDate.of(1991,10,1)), Some(PEN))
object PLZ extends Currency("PLZ", "Polish zloty A/94", "", 0, Some(LocalDate.of(1950,10,30)), Some(LocalDate.of(1994,12,31)), Some(PLN))
object TPE extends Currency("TPE", "Portuguese Timorese escudo", "", 0, Some(LocalDate.of(1959,1,1)), Some(LocalDate.of(1976,1,1)), Some(USD))
object ROL extends Currency("ROL", "Romanian leu A/05", "", 0, Some(LocalDate.of(1952,1,28)), Some(LocalDate.of(2005,1,1)), Some(RON))
object RUR extends Currency("RUR", "Russian ruble A/97", "", 2, Some(LocalDate.of(1992,1,1)), Some(LocalDate.of(1997,12,31)), Some(RUB))
object SDP extends Currency("SDP", "Sudanese old pound", "", 0, Some(LocalDate.of(1956,1,1)), Some(LocalDate.of(1992,6,8)), Some(SDD))
object SDD extends Currency("SDD", "Sudanese dinar", "", 0, Some(LocalDate.of(1992,6,8)), Some(LocalDate.of(2007,1,10)), Some(SDG))
object SRG extends Currency("SRG", "Suriname guilder", "", 0, None, Some(LocalDate.of(2004,1,1)), Some(SRD))
object SUR extends Currency("SUR", "Soviet Union ruble", "", 0, Some(LocalDate.of(1961,1,1)), Some(LocalDate.of(1991,1,1)), Some(RUR))
object TJR extends Currency("TJR", "Tajikistani ruble", "", 0, Some(LocalDate.of(1995,5,10)), Some(LocalDate.of(2000,10,30)), Some(TJS))
object TMM extends Currency("TMM", "Turkmenistani manat", "", 0, Some(LocalDate.of(1993,11,1)), Some(LocalDate.of(2008,12,31)), Some(TMT))
object TRL extends Currency("TRL", "Turkish lira A/05", "", 0, Some(LocalDate.of(1843,1,1)), Some(LocalDate.of(2005,1,1)), Some(TRY))
object UAK extends Currency("UAK", "Ukrainian karbovanets", "", 0, Some(LocalDate.of(1992,10,1)), Some(LocalDate.of(1996,9,1)), Some(UAH))
object UGS extends Currency("UGS", "Ugandan shilling A/87", "", 0, None, Some(LocalDate.of(1987,1,1)), Some(UGX))
object USS extends Currency("USS", "United States dollar (same day) (funds code)[24]", "", 2, None, Some(LocalDate.of(2014,3,28)), None)
object UYP extends Currency("UYP", "Uruguay peso", "", 0, Some(LocalDate.of(1896,1,1)), Some(LocalDate.of(1975,7,1)), Some(UYN))
object UYN extends Currency("UYN", "Uruguay new peso", "", 0, Some(LocalDate.of(1975,7,1)), Some(LocalDate.of(1993,3,1)), Some(UYU))
object VEB extends Currency("VEB", "Venezuelan bolívar", "", 2, None, Some(LocalDate.of(2008,1,1)), Some(VEF))
object XFO extends Currency("XFO", "Gold franc (special settlement currency)", "", 0, Some(LocalDate.of(1803,1,1)), Some(LocalDate.of(2003,1,1)), Some(XDR))
object YDD extends Currency("YDD", "South Yemeni dinar", "", 0, None, Some(LocalDate.of(1996,6,11)), Some(YER))
object YUD extends Currency("YUD", "Yugoslav dinar A/1989", "", 2, Some(LocalDate.of(1966,1,1)), Some(LocalDate.of(1989,12,31)), Some(YUN))
object YUN extends Currency("YUN", "Yugoslav dinar A/1992", "", 2, Some(LocalDate.of(1990,1,1)), Some(LocalDate.of(1992,6,30)), Some(YUR))
object YUR extends Currency("YUR", "Yugoslav dinar A/1993-09", "", 2, Some(LocalDate.of(1992,7,1)), Some(LocalDate.of(1993,9,30)), Some(YUO))
object YUO extends Currency("YUO", "Yugoslav dinar A/1993-12", "", 2, Some(LocalDate.of(1993,10,1)), Some(LocalDate.of(1993,12,31)), Some(YUG))
object YUG extends Currency("YUG", "Yugoslav dinar A/1994", "", 2, Some(LocalDate.of(1994,1,1)), Some(LocalDate.of(1994,1,23)), Some(YUM))
object YUM extends Currency("YUM", "Yugoslav dinar A/2003", "", 2, Some(LocalDate.of(1994,1,24)), Some(LocalDate.of(2003,7,2)), Some(CSD))
object ZAL extends Currency("ZAL", "South African financial rand (funds code)", "", 0, Some(LocalDate.of(1985,9,1)), Some(LocalDate.of(1995,3,13)), None)
object ZMK extends Currency("ZMK", "Zambian kwacha", "", 2, Some(LocalDate.of(1968,1,16)), Some(LocalDate.of(2013,1,1)), Some(ZMW))
object ZRN extends Currency("ZRN", "Zairean new zaire", "", 2, Some(LocalDate.of(1993,1,1)), Some(LocalDate.of(1997,1,1)), Some(CDF))
object ZRZ extends Currency("ZRZ", "Zairean zaire", "", 3, Some(LocalDate.of(1967,1,1)), Some(LocalDate.of(1993,1,1)), Some(ZRN))
object ZWC extends Currency("ZWC", "Rhodesian dollar", "", 2, Some(LocalDate.of(1970,2,17)), Some(LocalDate.of(1980,1,1)), Some(ZWD))
object ZWD extends Currency("ZWD", "Zimbabwean dollar A/06", "", 2, Some(LocalDate.of(1980,4,18)), Some(LocalDate.of(2006,7,31)), Some(ZWN))
object ZWN extends Currency("ZWN", "Zimbabwean dollar A/08", "", 2, Some(LocalDate.of(2006,8,1)), Some(LocalDate.of(2008,7,31)), Some(ZWR))
object ZWR extends Currency("ZWR", "Zimbabwean dollar A/09", "", 2, Some(LocalDate.of(2008,8,1)), Some(LocalDate.of(2009,2,2)), Some(ZWL))




/*
 * Other currencies
 */
object BTC extends Currency("BTC", "BitCoin", "B", 15, None, None, None)

object BKZ extends Currency("BKZ", "SpaceQuest Buckazoid", "bz", 2, None, None, None)


