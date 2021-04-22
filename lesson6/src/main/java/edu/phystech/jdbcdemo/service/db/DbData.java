package edu.phystech.jdbcdemo.service.db;

import edu.phystech.jdbcdemo.domain.*;
import lombok.var;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DbData {
    public static Collection<Aircrafts> getTestAircrafts() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/aircrafts.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<Aircrafts>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                result.add(new Aircrafts(list.get(0), list.get(1), Integer.parseInt(list.get(3))));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }
    public static Collection<Airports> getTestAirports() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/airports.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<Airports>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                result.add(new Airports(list.get(0), list.get(1)+","+list.get(2),list.get(3)+","+list.get(4), list.get(5)+","+list.get(6),list.get(7)));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }

    public static Collection<BoardingPasses> getTestBoardingPasses() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/boarding_passes.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<BoardingPasses>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                result.add(new BoardingPasses(list.get(0), Integer.parseInt(list.get(1)), Integer.parseInt(list.get(2)), list.get(3)));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }

    public static Collection<Bookings> getTestBookings() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/bookings.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<Bookings>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx").withZone(ZoneOffset.UTC);
                result.add(new Bookings(list.get(0), OffsetDateTime.from(formatter.parse(list.get(1))), Double.parseDouble(list.get(2))));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }

    public static Collection<Flights> getTestFlights() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/flights.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<Flights>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx").withZone(ZoneOffset.UTC);
                result.add(new Flights(Integer.parseInt(list.get(0)), list.get(1), OffsetDateTime.from(formatter.parse(list.get(2))),
                        OffsetDateTime.from(formatter.parse(list.get(3))), list.get(4), list.get(5), list.get(6),
                        list.get(7), list.get(8).equals("") ? null : OffsetDateTime.from(formatter.parse(list.get(8))),
                        list.get(9).equals("") ? null : OffsetDateTime.from(formatter.parse(list.get(9)))));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }

    public static Collection<Seats> getTestSeats() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/seats.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<Seats>();
        for (CSVRecord record : parser) {
            try {
                printer.printRecord(record);
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx").withZone(ZoneOffset.UTC);
                TemporalAccessor dateTime = formatter.parse("2017-07-05 03:12:00+06");
                OffsetDateTime.from(dateTime);
                result.add(new Seats(list.get(0), list.get(1), list.get(2)));
                System.out.print(list);
                System.out.print("\n");
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }

    public static Collection<TicketFlights> getTestTicketFlights() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/ticket_flights.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<TicketFlights>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx").withZone(ZoneOffset.UTC);
                result.add(new TicketFlights(list.get(0), Integer.parseInt(list.get(1)), list.get(2), Double.parseDouble(list.get(3))));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }

    public static Collection<Tickets> getTestTickets() throws IOException {
        Uploader test = new Uploader();
        var a = test.download(new URL("https://storage.yandexcloud.net/airtrans-small/tickets.csv"));
        CSVParser parser = CSVFormat.newFormat(',').parse(
                new InputStreamReader(new ByteArrayInputStream(a), "UTF8"));
        CSVPrinter printer = CSVFormat.newFormat(',').print(System.out);
        var result = new ArrayList<Tickets>();
        for (CSVRecord record : parser) {
            try {
                List<String> list = ExtensionData.getListFromIterator(record.iterator());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx").withZone(ZoneOffset.UTC);
                result.add(new Tickets(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4)));
            } catch (Exception e) {
                throw new RuntimeException("Error at line "
                        + parser.getCurrentLineNumber(), e);
            }
        }
        parser.close();
        printer.close();
        return result;
    }
}
