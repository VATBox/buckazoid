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

/*
 * Other currencies
 */
object BTC extends Currency("BTC", "BitCoin", "B", 15, None, None, None)

object BKZ extends Currency("BKZ", "SpaceQuest Buckazoid", "bz", 2, None, None, None)
