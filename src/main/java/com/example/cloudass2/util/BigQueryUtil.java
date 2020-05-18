package com.example.cloudass2.util;

import com.google.cloud.bigquery.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author miaos
 */
public class BigQueryUtil {

    private final String[] mainCountry = new String[]{"UK","India","France","Australia","China","US","Brazil"};

    public BigQueryUtil(){
    }

    public HashMap<String, COVIDnode> getCOD19(String countryName) throws InterruptedException {

        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);
        String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());

        Calendar cal1   =   Calendar.getInstance();
        cal1.add(Calendar.DATE,   -2);
        String theDayBeforeYesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal1.getTime());

        String sql = "SELECT "
                + "province_state, "
                + "confirmed AS feb_confirmed_cases, "
                + "deaths,"
                + "recovered,"
                + "FROM `bigquery-public-data.covid19_jhu_csse.summary`  "
                + "WHERE country_region = \""+countryName+"\" AND date ="
                +"\""+yesterday +"\"";

        String sql1 =  "SELECT "
                + "province_state, "
                + "confirmed AS feb_confirmed_cases, "
                + "FROM `bigquery-public-data.covid19_jhu_csse.summary`  "
                + "WHERE country_region = \""+countryName+"\" AND date ="
                +"\""+theDayBeforeYesterday +"\"";


        HashMap<String,COVIDnode> map= new HashMap<>();
        TableResult result = executeQuery(sql);
        TableResult result1 = executeQuery(sql1);

        // Print all pages of the results.
        for (FieldValueList row : result.iterateAll()) {
            String province_state = row.get("province_state").getStringValue();
            long cases = row.get("feb_confirmed_cases").getLongValue();
            long death = row.get("deaths").getLongValue();
            long cure = row.get("recovered").getLongValue();

            COVIDnode coviDnode = new COVIDnode(province_state,cases,cure,death);
            map.put(province_state,coviDnode);
        }

        for (FieldValueList row : result1.iterateAll()) {
            String province_state = row.get("province_state").getStringValue();
            long cases = row.get("feb_confirmed_cases").getLongValue();
            if(map.containsKey(province_state)) {
                map.get(province_state).setIncreased(cases);
            }
        }

        return map;
    }

    private TableResult executeQuery(String sql) throws InterruptedException {
        BigQuery bigquery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration queryConfig =
                QueryJobConfiguration.newBuilder(
                        sql)
                        // Use standard SQL syntax for queries.
                        // See: https://cloud.google.com/bigquery/sql-reference/
                        .setUseLegacySql(false)
                        .build();
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());
        queryJob = queryJob.waitFor();
        if (queryJob == null) {
            throw new RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            // You can also look at queryJob.getStatus().getExecutionErrors() for all
            // errors, not just the latest one.
            throw new RuntimeException(queryJob.getStatus().getError().toString());
        }
        return queryJob.getQueryResults();
    }
}
