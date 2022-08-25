public class Main {

    public static void main(String[] args){
        LibGen lg = new LibGen();
        ProjectGutenBerg pg = new ProjectGutenBerg();

        /**
         * I have commented out the call to run libgen off the hop as I worry some users
         * may not take the time to understand the code or what is taking place.
         * To run it, just uncommented the line.
         */


        //lg.search_Libgen("herbal");
        pg.search_ProjectGutenberg("herbal");


    }
}
