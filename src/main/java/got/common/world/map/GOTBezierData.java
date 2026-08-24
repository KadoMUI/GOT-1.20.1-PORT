package got.common.world.map;

/** Extracted from the original 1.7.10 GOTBeziers.onInit database. */
final class GOTBezierData {
    private GOTBezierData() {}
    static void registerAll() {
        registerRoutes0();
        registerRoutes1();
        registerRoutes2();
        registerRoutes3();
        registerRoutes4();
        registerRoutes5();
        registerRoutes6();
        registerRoutes7();
        registerRoutes8();
        registerLinkers0();
        registerLinkers1();
        registerLinkers2();
        registerLinkers3();
        registerLinkers4();
    }
    private static void registerRoutes0() {
        GOTBeziers.registerBezier(GOTBeziers.Type.WALL, GOTWaypoint.WESTWATCH, GOTWaypoint.SHADOW_TOWER, GOTWaypoint.SENTINEL_STAND, GOTWaypoint.GREYGUARD, GOTWaypoint.STONEDOOR, GOTWaypoint.HOARFROST_HILL, GOTWaypoint.ICEMARK, GOTWaypoint.NIGHTFORT, GOTWaypoint.DEEP_LAKE, GOTWaypoint.QUEENSGATE, GOTWaypoint.CASTLE_BLACK, GOTWaypoint.OAKENSHIELD, GOTWaypoint.WOODSWATCH, GOTWaypoint.SABLE_HALL, GOTWaypoint.RIMEGATE, GOTWaypoint.THE_LONG_BARROW, GOTWaypoint.THE_TORCHES, GOTWaypoint.GREENGUARD, GOTWaypoint.EASTWATCH);
        GOTBeziers.registerBezier(GOTBeziers.Type.WALL, GOTWaypoint.ANBEI, GOTWaypoint.JIANMEN, GOTWaypoint.ANGUO, GOTWaypoint.ANJIANG, GOTWaypoint.DINGGUO, GOTWaypoint.PINNU, GOTWaypoint.PINGJIANG, GOTWaypoint.WUDE, GOTWaypoint.WUSHENG, GOTWaypoint.ZHENGUO, GOTWaypoint.LUNGMEN, GOTWaypoint.PINGBEI);
        GOTBeziers.registerBezier(GOTBeziers.Type.WALL, new double[]{2847D, 1273D}, new double[]{2820D, 1292D}, new double[]{2771D, 1308D}, new double[]{2732D, 1308D});
        GOTBeziers.registerBezier(GOTBeziers.Type.WALL, new double[]{2732D, 1308D}, new double[]{2683D, 1294D}, new double[]{2628D, 1294D}, new double[]{2588D, 1275D});
        GOTBeziers.registerBezier(GOTBeziers.Type.WALL, new double[]{2708D, 1230D}, new double[]{2683D, 1244D}, new double[]{2656D, 1253D}, new double[]{2638D, 1252D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{559D, 544D}, GOTWaypoint.SKIRLING_PASS, new double[]{596D, 544D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CASTLE_BLACK, new double[]{745D, 732D}, new double[]{694D, 804D}, GOTWaypoint.WINTERFELL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.WINTERFELL, new double[]{642D, 891D}, GOTWaypoint.CASTLE_CERWYN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CASTLE_CERWYN, new double[]{696D, 899D}, new double[]{765D, 872D}, GOTWaypoint.DREADFORT);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.DREADFORT, new double[]{855D, 865D}, new double[]{893D, 828D}, GOTWaypoint.KARHOLD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CASTLE_CERWYN, new double[]{632D, 976D}, new double[]{644D, 1043D}, GOTWaypoint.MOAT_KAILIN.info(0D, -0.5D));
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TORRHENS_SQUARE, new double[]{553D, 996D}, new double[]{539D, 1037D}, GOTWaypoint.GOLDGRASS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.RILLWATER_CROSSING, new double[]{422D, 989D}, new double[]{419D, 1013D}, GOTWaypoint.RYSWELLS_CASTLE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.RYSWELLS_CASTLE, new double[]{447D, 1064D}, new double[]{496D, 1066D}, GOTWaypoint.GOLDGRASS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GOLDGRASS, new double[]{606D, 1081D}, new double[]{626D, 1103D}, GOTWaypoint.MOAT_KAILIN.info(-0.5D, 0D));
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MOAT_KAILIN.info(0.5D, 0D), new double[]{672D, 1101D}, new double[]{707D, 1072D}, GOTWaypoint.WHITE_HARBOUR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MOAT_KAILIN.info(0D, 0.5D), new double[]{649D, 1136D}, new double[]{656D, 1179D}, new double[]{655D, 1257D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MOAT_KAILIN.info(-0.5D, 0D), GOTWaypoint.MOAT_KAILIN.info(0.5D, 0D));
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MOAT_KAILIN.info(0D, -0.5D), GOTWaypoint.MOAT_KAILIN.info(0D, 0.5D));
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CROSSROADS_INN, new double[]{754D, 1430D}, new double[]{771D, 1417D}, GOTWaypoint.BLOODY_GATE);
    }
    private static void registerRoutes1() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.BLOODY_GATE, new double[]{804D, 1397D}, new double[]{817D, 1388D}, GOTWaypoint.GATE_OF_THE_MOON);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GATE_OF_THE_MOON, new double[]{827D, 1369D}, new double[]{828D, 1361D}, GOTWaypoint.THE_EYRIE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{655D, 1257D}, new double[]{638D, 1268D}, new double[]{623D, 1282D}, GOTWaypoint.TWINS_RIGHT);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TWINS_RIGHT, GOTWaypoint.TWINS_LEFT);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TWINS_LEFT, new double[]{590D, 1296D}, new double[]{583D, 1311D}, GOTWaypoint.SEAGARD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{655D, 1257D}, new double[]{664D, 1310D}, new double[]{683D, 1360D}, new double[]{713D, 1400D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{713D, 1400D}, new double[]{721D, 1412D}, new double[]{723D, 1428D}, GOTWaypoint.CROSSROADS_INN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CROSSROADS_INN, new double[]{758D, 1446D}, new double[]{776D, 1461D}, GOTWaypoint.SALTPANS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CROSSROADS_INN, new double[]{732D, 1447D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{732D, 1447D}, new double[]{730D, 1463D}, new double[]{735D, 1479D}, GOTWaypoint.WHITEWALLS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.WHITEWALLS, new double[]{726D, 1490D}, GOTWaypoint.HARRENHAL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.WHITEWALLS, new double[]{751D, 1503D}, new double[]{761D, 1513D}, GOTWaypoint.HOGG_HALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HOGG_HALL, GOTWaypoint.ANTLERS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{732D, 1447D}, GOTWaypoint.HARROWAY);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HARROWAY, new double[]{703D, 1443D}, new double[]{685D, 1446D}, GOTWaypoint.CASTLE_LYCHESTER);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CASTLE_LYCHESTER, new double[]{657D, 1450D}, new double[]{644D, 1458D}, GOTWaypoint.ACORN_HALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.ACORN_HALL, new double[]{628D, 1478D}, GOTWaypoint.WAYFARERS_REST);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CASTLE_LYCHESTER, new double[]{655D, 1439D}, new double[]{642D, 1430D}, GOTWaypoint.STONE_HEDGE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.STONE_HEDGE, new double[]{615D, 1431D}, new double[]{601D, 1434D}, GOTWaypoint.RIVERRUN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.RIVERRUN, new double[]{565D, 1444D}, new double[]{541D, 1462D}, new double[]{519D, 1476D});
    }
    private static void registerRoutes2() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.STONEY_SEPT, new double[]{691D, 1565D}, GOTWaypoint.BRIARWHITE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.BRIARWHITE, new double[]{740D, 1588D}, new double[]{748D, 1606D}, GOTWaypoint.HAYFORD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HAYFORD, new double[]{767D, 1624D}, GOTWaypoint.KINGS_LANDING);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.ANTLERS, new double[]{799D, 1547D}, new double[]{809D, 1569D}, GOTWaypoint.STOKEWORTH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.STOKEWORTH, new double[]{818D, 1584D}, new double[]{826D, 1581D}, GOTWaypoint.DUSKENDALE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.DUSKENDALE, new double[]{837D, 1573D}, new double[]{840D, 1569D}, GOTWaypoint.HOLLARD_CASTLE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HOLLARD_CASTLE, new double[]{848D, 1552D}, new double[]{855D, 1542D}, GOTWaypoint.ROOKS_REST);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.STOKEWORTH, new double[]{807D, 1597D}, GOTWaypoint.ROSBY);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.ROSBY, new double[]{793D, 1614D}, new double[]{778D, 1617D}, GOTWaypoint.KINGS_LANDING);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{519D, 1476D}, new double[]{510D, 1480D}, new double[]{501D, 1485D}, GOTWaypoint.GOLDEN_TOOTH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GOLDEN_TOOTH, new double[]{476D, 1504D}, new double[]{460D, 1513D}, GOTWaypoint.SARSFIELD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SARSFIELD, GOTWaypoint.OXCROSS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.OXCROSS, GOTWaypoint.CASTERLY_ROCK);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CASTERLY_ROCK, GOTWaypoint.LANNISPORT);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LANNISPORT, new double[]{371D, 1607D}, new double[]{366D, 1639D}, GOTWaypoint.CRAKEHALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CRAKEHALL, new double[]{349D, 1681D}, new double[]{359D, 1700D}, new double[]{363D, 1716D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LANNISPORT, new double[]{410D, 1572D}, new double[]{453D, 1569D}, new double[]{489D, 1577D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{489D, 1577D}, new double[]{518D, 1592D}, new double[]{543D, 1612D}, new double[]{574D, 1623D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{363D, 1716D}, new double[]{365D, 1730D}, new double[]{371D, 1743D}, GOTWaypoint.OLD_OAK);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{574D, 1623D}, GOTWaypoint.HAMMERHAL, GOTWaypoint.IVY_HALL);
    }
    private static void registerRoutes3() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{574D, 1623D}, new double[]{640D, 1627D}, new double[]{705D, 1625D}, GOTWaypoint.KINGS_LANDING);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SMITHYTON, new double[]{707D, 1689D}, new double[]{704D, 1680D}, GOTWaypoint.TUMBLETON);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TUMBLETON, new double[]{687D, 1673D}, new double[]{673D, 1673D}, GOTWaypoint.RING);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SMITHYTON, GOTWaypoint.BITTERBRIDGE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.BITTERBRIDGE, new double[]{625D, 1714D}, new double[]{596D, 1727D}, GOTWaypoint.APPLETON);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.APPLETON, new double[]{537D, 1768D}, GOTWaypoint.DARKDELL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.DARKDELL, new double[]{512D, 1794D}, new double[]{501D, 1803D}, GOTWaypoint.HIGHGARDEN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.DARKDELL, new double[]{512D, 1772D}, new double[]{498D, 1768D}, GOTWaypoint.HOLYHALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HOLYHALL, new double[]{491D, 1745D}, new double[]{485D, 1730D}, GOTWaypoint.COLDMOAT);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.COLDMOAT, new double[]{459D, 1720D}, new double[]{446D, 1714D}, GOTWaypoint.RED_LAKE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.RED_LAKE, new double[]{410D, 1711D}, new double[]{387D, 1713D}, new double[]{363D, 1716D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HIGHGARDEN, new double[]{490D, 1830D}, new double[]{487D, 1844D}, GOTWaypoint.WHITEGROVE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.WHITEGROVE, new double[]{459D, 1887D}, new double[]{419D, 1915D}, GOTWaypoint.OLDTOWN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.OLDTOWN, new double[]{383D, 1968D}, new double[]{376D, 1983D}, GOTWaypoint.THREE_TOWERS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.THREE_TOWERS, new double[]{354D, 2006D}, GOTWaypoint.GARNETGROVE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GARNETGROVE, new double[]{378D, 2026D}, new double[]{394D, 2046D}, GOTWaypoint.SUNHOUSE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SUNHOUSE, new double[]{449D, 2034D}, new double[]{475D, 2022D}, GOTWaypoint.STARFALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.WHITEGROVE, new double[]{521D, 1857D}, new double[]{550D, 1870D}, GOTWaypoint.NIGHTSONG);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SMITHYTON, new double[]{732D, 1692D}, new double[]{752D, 1677D}, new double[]{775D, 1666D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.STARFALL, new double[]{507D, 1994D}, new double[]{510D, 1987D}, GOTWaypoint.HIGH_HERMITAGE);
    }
    private static void registerRoutes4() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HIGH_HERMITAGE, new double[]{516D, 1966D}, new double[]{520D, 1955D}, GOTWaypoint.BLACKMONT);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.YRONWOOD, new double[]{688D, 1954D}, new double[]{715D, 1921D}, GOTWaypoint.WYL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.WYL, new double[]{734D, 1894D}, new double[]{726D, 1884D}, GOTWaypoint.BLACKHAVEN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.NIGHTSONG, new double[]{607D, 1870D}, new double[]{625D, 1861D}, GOTWaypoint.HARVEST_HALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HARVEST_HALL, new double[]{654D, 1849D}, new double[]{665D, 1846D}, GOTWaypoint.PODDINGFIELD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.PODDINGFIELD, new double[]{698D, 1842D}, new double[]{719D, 1841D}, new double[]{741D, 1840D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.NIGHTSONG, new double[]{593D, 1886D}, new double[]{601D, 1898D}, GOTWaypoint.TOWER_OF_JOY);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TOWER_OF_JOY, GOTWaypoint.KINGSGRAVE, GOTWaypoint.SKYREACH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.BLACKHAVEN, new double[]{729D, 1861D}, new double[]{735D, 1851D}, new double[]{741D, 1840D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{741D, 1840D}, new double[]{755D, 1824D}, new double[]{769D, 1811D}, GOTWaypoint.SUMMERHALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.KINGS_LANDING, new double[]{775D, 1666D}, GOTWaypoint.GRANDVIEW, GOTWaypoint.SUMMERHALL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GRANDVIEW, GOTWaypoint.FELWOOD, GOTWaypoint.HAYSTACK_HALL, GOTWaypoint.GALLOWSGREY, GOTWaypoint.PARCHMENTS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FELWOOD, new double[]{854D, 1745D}, GOTWaypoint.BRONZEGATE, GOTWaypoint.STORMS_END);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.ZAMETTAR, new double[]{2164D, 2749D}, new double[]{2166D, 2770D}, new double[]{2176D, 2793D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{2176D, 2793D}, new double[]{2189D, 2807D}, new double[]{2195D, 2819D}, GOTWaypoint.YEEN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LHAZOSH, new double[]{2474D, 2056D}, new double[]{2462D, 2111D}, new double[]{2463D, 2153D}, new double[]{2483D, 2183D}, new double[]{2522D, 2203D}, GOTWaypoint.VAES_ORVIK);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LHAZOSH, new double[]{2492D, 1993D}, new double[]{2528D, 1976D}, GOTWaypoint.KOSRAK);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MEEREEN, new double[]{2297D, 1945D}, new double[]{2334D, 1930D}, new double[]{2373D, 1919D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.ASTAPOR, new double[]{2190D, 2130D}, new double[]{2196D, 2111D}, new double[]{2203D, 2090D}, new double[]{2206D, 2059D}, new double[]{2196D, 2028D}, GOTWaypoint.YUNKAI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.YUNKAI, new double[]{2224D, 1993D}, new double[]{2236D, 1970D}, GOTWaypoint.MEEREEN.info(-0.5D, 0D));
    }
    private static void registerRoutes5() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MEEREEN.info(-0.5D, 0D), new double[]{2230D, 1933D}, new double[]{2158D, 1937D}, GOTWaypoint.BHORASH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MEEREEN, GOTWaypoint.MEEREEN.info(-0.5D, 0D));
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_JINI, new double[]{2916D, 1827D}, new double[]{2956D, 1844D}, GOTWaypoint.SAMYRIANA);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_MEJHAH, new double[]{2662D, 1819D}, new double[]{2749D, 1823D}, GOTWaypoint.VAES_JINI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.KRAZAAJ_HAS, new double[]{2518D, 1838D}, new double[]{2544D, 1832D}, GOTWaypoint.VAES_MEJHAH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{2373D, 1919D}, new double[]{2413D, 1902D}, new double[]{2449D, 1867D}, GOTWaypoint.KRAZAAJ_HAS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{2373D, 1919D}, GOTWaypoint.HESH, GOTWaypoint.LHAZOSH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_ATHJIKHARI, new double[]{2237D, 1543D}, new double[]{2255D, 1559D}, GOTWaypoint.VAES_LEQSE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_LEQSE, new double[]{2263D, 1592D}, new double[]{2272D, 1611D}, GOTWaypoint.SATHAR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SATHAR, new double[]{2251D, 1639D}, new double[]{2199D, 1649D}, GOTWaypoint.VOJJOR_SAMVI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VOJJOR_SAMVI, new double[]{2111D, 1653D}, new double[]{2068D, 1642D}, GOTWaypoint.VAES_KHEWO);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_KHEWO, new double[]{1991D, 1631D}, new double[]{1953D, 1630D}, GOTWaypoint.VAES_GORQOYI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SAATH, new double[]{1879D, 1373D}, new double[]{1887D, 1423D}, GOTWaypoint.KYTH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.KYTH, new double[]{1925D, 1469D}, new double[]{1936D, 1487D}, GOTWaypoint.HORNOTH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.HORNOTH, new double[]{1933D, 1521D}, new double[]{1902D, 1543D}, GOTWaypoint.RATHYLAR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_GORQOYI, new double[]{1907D, 1604D}, new double[]{1903D, 1584D}, GOTWaypoint.RATHYLAR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_KHADOKH, new double[]{1796D, 1580D}, new double[]{1846D, 1576D}, GOTWaypoint.RATHYLAR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{2978D, 2279D}, new double[]{3002D, 2284D}, new double[]{3036D, 2247D}, new double[]{3078D, 2240D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{2928D, 2260D}, new double[]{2948D, 2260D}, new double[]{2965D, 2267D}, new double[]{2978D, 2279D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.QARTH, new double[]{2884D, 2288D}, new double[]{2901D, 2268D}, new double[]{2928D, 2260D});
    }
    private static void registerRoutes6() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.QARKASH, new double[]{2735D, 2265D}, new double[]{2766D, 2288D}, new double[]{2807D, 2285D}, new double[]{2840D, 2275D}, new double[]{2864D, 2290D}, GOTWaypoint.QARTH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.PORT_YHOS, new double[]{2622D, 2260D}, new double[]{2648D, 2243D}, new double[]{2665D, 2234D}, new double[]{2685D, 2232D}, new double[]{2710D, 2239D}, GOTWaypoint.QARKASH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAES_ORVIK, new double[]{2571D, 2255D}, new double[]{2563D, 2268D}, GOTWaypoint.PORT_YHOS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MANTARYS, new double[]{1865D, 2062D}, new double[]{1831D, 2099D}, new double[]{1816D, 2145D}, new double[]{1811D, 2181D}, new double[]{1797D, 2228D}, new double[]{1801D, 2246D}, new double[]{1815D, 2249D}, new double[]{1831D, 2247D}, GOTWaypoint.OROS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.BHORASH, new double[]{2024D, 1981D}, new double[]{1988D, 2001D}, new double[]{1956D, 2026D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{1956D, 2026D}, new double[]{1958D, 2043D}, new double[]{1951D, 2058D}, GOTWaypoint.TOLOS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{1956D, 2026D}, new double[]{1942D, 2030D}, new double[]{1913D, 2027D}, GOTWaypoint.MANTARYS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.MANTARYS, new double[]{1839D, 2004D}, new double[]{1802D, 2007D}, GOTWaypoint.ANOGARIA);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.ANOGARIA, new double[]{1738D, 2022D}, new double[]{1710D, 2030D}, GOTWaypoint.LITTLE_VALYRIA);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.QOHOR, new double[]{1666D, 1606D}, new double[]{1732D, 1603D}, GOTWaypoint.VAES_KHADOKH);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.QOHOR, new double[]{1625D, 1637D}, new double[]{1562D, 1661D}, GOTWaypoint.AR_NOY);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CHROYANE, new double[]{1437D, 1844D}, new double[]{1403D, 1861D}, new double[]{1354D, 1868D}, new double[]{1315D, 1864D}, new double[]{1286D, 1853D}, GOTWaypoint.MYR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LITTLE_VALYRIA, new double[]{1661D, 2022D}, GOTWaypoint.VALYRIAN_ROAD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LITTLE_VALYRIA, new double[]{1638D, 2054D}, new double[]{1589D, 2063D}, GOTWaypoint.VOLANTIS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VOLANTIS, new double[]{1562D, 2049D}, new double[]{1557D, 2045D}, GOTWaypoint.SAR_MELL);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SAR_MELL, GOTWaypoint.VOLON_THERYS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VOLON_THERYS, new double[]{1519D, 2032D}, new double[]{1508D, 2021D}, GOTWaypoint.VALYSAR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VALYSAR, new double[]{1496D, 1988D}, new double[]{1492D, 1963D}, GOTWaypoint.SELHORYS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SELHORYS, new double[]{1501D, 1916D}, new double[]{1491D, 1897D}, new double[]{1486D, 1879D}, new double[]{1484D, 1862D}, new double[]{1484D, 1846D}, GOTWaypoint.CHROYANE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CHROYANE, new double[]{1499D, 1802D}, new double[]{1505D, 1730D}, GOTWaypoint.AR_NOY);
    }
    private static void registerRoutes7() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.AR_NOY, new double[]{1479D, 1682D}, new double[]{1453D, 1660D}, GOTWaypoint.NY_SAR);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.NY_SAR, new double[]{1430D, 1617D}, new double[]{1417D, 1583D}, new double[]{1423D, 1552D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{1423D, 1552D}, new double[]{1425D, 1528D}, new double[]{1417D, 1510D}, GOTWaypoint.NORVOS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.NY_SAR, new double[]{1391D, 1635D}, new double[]{1356D, 1612D}, GOTWaypoint.GHOYAN_DROHE);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GHOYAN_DROHE, new double[]{1262D, 1604D}, GOTWaypoint.PENTOS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.GHOYAN_DROHE, new double[]{1315D, 1539D}, new double[]{1263D, 1493D}, new double[]{1250D, 1451D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{1250D, 1451D}, new double[]{1246D, 1421D}, new double[]{1223D, 1376D}, new double[]{1210D, 1339D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{1210D, 1339D}, new double[]{1197D, 1304D}, new double[]{1175D, 1270D}, GOTWaypoint.BRAAVOS);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FIVE_FORTS_1, GOTWaypoint.FIVE_FORTS_2);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FIVE_FORTS_2, GOTWaypoint.FIVE_FORTS_3);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FIVE_FORTS_3, GOTWaypoint.FIVE_FORTS_4);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FIVE_FORTS_4, GOTWaypoint.FIVE_FORTS_5);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{3078D, 2240D}, new double[]{3092D, 2210D}, new double[]{3098D, 2164D}, GOTWaypoint.EIJIANG);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{3078D, 2240D}, new double[]{3083D, 2244D}, new double[]{3086D, 2255D}, GOTWaypoint.ASABHAD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.EIJIANG, new double[]{3130D, 2131D}, new double[]{3133D, 2100D}, GOTWaypoint.BAYASABHAD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CHANGAN, new double[]{3212D, 2373D}, new double[]{3223D, 2391D}, new double[]{3235D, 2406D}, new double[]{3256D, 2425D}, new double[]{3277D, 2438D}, GOTWaypoint.YIN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.CHANGAN, new double[]{3180D, 2355D}, new double[]{3158D, 2330D}, new double[]{3132D, 2316D}, new double[]{3111D, 2299D}, new double[]{3100D, 2270D}, GOTWaypoint.ASABHAD);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SI_QO, new double[]{3259D, 2291D}, new double[]{3227D, 2311D}, GOTWaypoint.CHANGAN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{3729D, 2423D}, new double[]{3757D, 2401D}, new double[]{3783D, 2361D}, GOTWaypoint.YUNNAN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, new double[]{3674D, 2408D}, new double[]{3695D, 2420D}, new double[]{3729D, 2423D}, new double[]{3762D, 2448D}, new double[]{3791D, 2494D}, new double[]{3807D, 2552D}, new double[]{3811D, 2608D}, new double[]{3803D, 2687D}, new double[]{3770D, 2754D}, GOTWaypoint.ASSHAI);
    }
    private static void registerRoutes8() {
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.JINQI, new double[]{3630D, 2391D}, new double[]{3658D, 2398D}, new double[]{3674D, 2408D});
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.EIJIANG, new double[]{3175D, 2141D}, new double[]{3246D, 2117D}, GOTWaypoint.TIQUI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SI_QO, new double[]{3276D, 2218D}, new double[]{3299D, 2178D}, GOTWaypoint.TIQUI.info(-1D, 0D));
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TIQUI.info(-1D, 0D), new double[]{3316D, 2024D}, new double[]{3348D, 1940D}, GOTWaypoint.TRADER_TOWN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.SAMYRIANA, new double[]{3144D, 1860D}, new double[]{3249D, 1873D}, GOTWaypoint.TRADER_TOWN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TRADER_TOWN, new double[]{3382D, 1864D}, new double[]{3383D, 1862D}, GOTWaypoint.ANJIANG);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TRADER_TOWN, new double[]{3398D, 1914D}, new double[]{3457D, 1960D}, GOTWaypoint.VAIBEI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.VAIBEI, new double[]{3553D, 2008D}, new double[]{3578D, 2068D}, GOTWaypoint.YIBIN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.YIBIN, new double[]{3732D, 2071D}, new double[]{3811D, 2011D}, GOTWaypoint.FIVE_FORTS_5);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.YIBIN, new double[]{3650D, 2108D}, new double[]{3663D, 2125D}, GOTWaypoint.LIZHAO);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.LIZHAO, new double[]{3672D, 2184D}, new double[]{3663D, 2220D}, GOTWaypoint.FU_NING);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FU_NING, new double[]{3623D, 2308D}, new double[]{3620D, 2341D}, GOTWaypoint.JINQI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.FU_NING, new double[]{3595D, 2246D}, new double[]{3561D, 2222D}, GOTWaypoint.BAOJI);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.BAOJI, new double[]{3481D, 2212D}, new double[]{3458D, 2206D}, GOTWaypoint.MANJIN);
        GOTBeziers.registerBezier(GOTBeziers.Type.ROAD, GOTWaypoint.TIQUI, new double[]{3390D, 2123D}, new double[]{3432D, 2160D}, GOTWaypoint.MANJIN);
    }
    private static void registerLinkers0() {
        GOTBeziers.registerLinker(GOTWaypoint.BITTERBRIDGE.info(0D, 0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.BLACKHAVEN.info(-0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.BLACKMONT.info(0.0453125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.BLOODY_GATE.info(0D, -0.1953125D));
        GOTBeziers.registerLinker(GOTWaypoint.CASTERLY_ROCK.info(-0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.CASTLE_CERWYN.info(-0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.DARKDELL.info(-0.1953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.DREADFORT.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.FELWOOD.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.GARNETGROVE.info(-0.0453125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.GOLDGRASS.info(0D, 0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.GRANDVIEW.info(-0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.HAYSTACK_HALL.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.HIGH_HERMITAGE.info(0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.HOLYHALL.info(0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.IVY_HALL.info(0D, 0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.KARHOLD.info(0.0453125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.KINGSGRAVE.info(0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.KINGS_LANDING.info(1.6953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.LANNISPORT.info(-0.14375D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.NIGHTSONG.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.OLD_OAK.info(0D, 0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.PARCHMENTS.info(0D, -0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.PODDINGFIELD.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.RED_LAKE.info(0D, 0.0953125D));
    }
    private static void registerLinkers1() {
        GOTBeziers.registerLinker(GOTWaypoint.RILLWATER_CROSSING.info(-0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.RING.info(0D, -0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.RIVERRUN.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.ROOKS_REST.info(0D, -0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.SALTPANS.info(0.14375D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.SEAGARD.info(0D, 0.14375D));
        GOTBeziers.registerLinker(GOTWaypoint.SKYREACH.info(0D, 0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.SMITHYTON.info(0D, 0.14375D));
        GOTBeziers.registerLinker(GOTWaypoint.STOKEWORTH.info(-0.0953125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.STONEY_SEPT.info(-0.14375D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.STONE_HEDGE.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.STORMS_END.info(0D, 0.1953125D));
        GOTBeziers.registerLinker(GOTWaypoint.SUNHOUSE.info(0D, 0.0453125D));
        GOTBeziers.registerLinker(GOTWaypoint.THE_EYRIE.info(0D, -0.0953125D));
        GOTBeziers.registerLinker(GOTWaypoint.THREE_TOWERS.info(-0.4D, -0.4D));
        GOTBeziers.registerLinker(GOTWaypoint.THREE_TOWERS.info(-0.4D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.THREE_TOWERS.info(-0.4D, 0.4D));
        GOTBeziers.registerLinker(GOTWaypoint.TORRHENS_SQUARE.info(-0.0453125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.WAYFARERS_REST.info(-0.0453125D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.WHITE_HARBOUR.info(0.14375D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.YRONWOOD.info(0.0453125D, 0D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.ACORN_HALL.info(-0.1453125D, -0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.ANTLERS.info(0.05D, -0.1953125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.APPLETON.info(-0.05D, -0.1953125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.APPLETON.info(0.1D, 0.44375D));
    }
    private static void registerLinkers2() {
        GOTBeziers.registerLinkerAuto(GOTWaypoint.BRONZEGATE.info(0.05D, -0.1953125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.COLDMOAT.info(-0.05D, 0.1453125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.CRAKEHALL.info(-0.1453125D, -0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.DUSKENDALE.info(-0.1D, -0.44375D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.GALLOWSGREY.info(-0.05D, -0.1453125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.GATE_OF_THE_MOON.info(0.1453125D, 0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.GOLDEN_TOOTH.info(-0.1D, -0.1453125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.HAMMERHAL.info(0.1453125D, -0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.HARROWAY.info(-0.1D, 0.24375D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.HARVEST_HALL.info(-0.05D, -0.1453125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.HAYFORD.info(0.05D, -0.1953125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.HIGHGARDEN.info(0.1453125D, 0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.OLDTOWN.info(-0.34375D, -0.2D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.ROSBY.info(-0.1453125D, -0.1D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.RYSWELLS_CASTLE.info(-0.1453125D, 0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.SARSFIELD.info(-0.1D, -0.1453125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.SEAGARD.info(-0.1D, -0.1953125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.STARFALL.info(0.1D, 0.1453125D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.TUMBLETON.info(0.1D, -0.29375D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.WHITEGROVE.info(-0.1453125D, -0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.WINTERFELL.info(-0.1453125D, -0.05D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.WYL.info(-0.1953125D, -0.05D));
        GOTBeziers.registerLinker(GOTWaypoint.ASTAPOR.info(-0.140625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.BRAAVOS.info(0D, -0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.MANTARYS.info(0D, -0.140625D));
    }
    private static void registerLinkers3() {
        GOTBeziers.registerLinker(GOTWaypoint.MEEREEN.info(0D, -0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.MYR.info(-0.140625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.NORVOS.info(0D, -0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.PENTOS.info(-0.140625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.PORT_YHOS.info(0D, 0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.QARKASH.info(0D, 0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.QARTH.info(0D, 0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.SELHORYS.info(-0.140625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.TOLOS.info(0D, 0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.VOLON_THERYS.info(0D, 0.140625D));
        GOTBeziers.registerLinker(GOTWaypoint.YUNKAI.info(-0.140625D, 0D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.QOHOR.info(-0.240625D, -0.1D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.VOLANTIS.info(-0.2D, 0.240625D));
        GOTBeziers.registerLinkerAutoInv(GOTWaypoint.ANOGARIA.info(0.3D, 0.240625D));
        GOTBeziers.registerLinkerAutoInv(GOTWaypoint.BHORASH.info(0.3D, 0.240625D));
        GOTBeziers.registerLinkerAutoInv(GOTWaypoint.LITTLE_VALYRIA.info(0.3D, 0.240625D));
        GOTBeziers.registerLinkerAutoInv(GOTWaypoint.VALYSAR.info(-0.240625D, 0.3D));
        GOTBeziers.registerLinker(GOTWaypoint.KOSRAK.info(1D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.LHAZOSH.info(1D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.HESH.info(1D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.ASABHAD.info(-0.165625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.BAOJI.info(0D, -0.165625D));
        GOTBeziers.registerLinker(GOTWaypoint.CHANGAN.info(0.165625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.EIJIANG.info(0D, 0.165625D));
        GOTBeziers.registerLinker(GOTWaypoint.JINQI.info(-0.165625D, 0D));
    }
    private static void registerLinkers4() {
        GOTBeziers.registerLinker(GOTWaypoint.LIZHAO.info(0.165625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.SI_QO.info(0.165625D, 0D));
        GOTBeziers.registerLinker(GOTWaypoint.TIQUI.info(0D, -0.165625D));
        GOTBeziers.registerLinker(GOTWaypoint.TRADER_TOWN.info(0D, -0.265625D));
        GOTBeziers.registerLinker(GOTWaypoint.VAIBEI.info(0D, -0.265625D));
        GOTBeziers.registerLinker(GOTWaypoint.YIBIN.info(0D, -0.165625D));
        GOTBeziers.registerLinker(GOTWaypoint.YIN.info(0D, 0.165625D));
        GOTBeziers.registerLinker(GOTWaypoint.YUNNAN.info(0.165625D, 0D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.FU_NING.info(0.365625D, 0.1D));
        GOTBeziers.registerLinkerAuto(GOTWaypoint.MANJIN.info(0.365625D, -0.2D));
        GOTBeziers.registerLinker(GOTWaypoint.ASSHAI.info(0D, 0.14375D));
        GOTBeziers.registerLinker(GOTWaypoint.ZAMETTAR.info(0D, -0.125D));
    }
}
