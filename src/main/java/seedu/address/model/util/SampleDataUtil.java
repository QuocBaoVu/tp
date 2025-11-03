package seedu.address.model.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Maplet;
import seedu.address.model.ReadOnlyMaplet;
import seedu.address.model.attraction.Activities;
import seedu.address.model.attraction.Address;
import seedu.address.model.attraction.Attraction;
import seedu.address.model.attraction.Comment;
import seedu.address.model.attraction.Contact;
import seedu.address.model.attraction.Name;
import seedu.address.model.attraction.OpeningHours;
import seedu.address.model.attraction.Price;
import seedu.address.model.attraction.Priority;
import seedu.address.model.itinerary.Itinerary;
import seedu.address.model.itinerary.ItineraryName;
import seedu.address.model.location.Location;
import seedu.address.model.location.LocationName;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code Maplet} with sample data.
 */
public class SampleDataUtil {

    public static Attraction[] getSampleAttractions() {
        return new Attraction[] {
            new Attraction(new Name("Universal Studios Singapore"),
                    new Priority("8"),
                    new Contact("testcontact@gmail.com"),
                    new Address("8 Sentosa Gateway 098269"),
                    new Activities("ride a rollercoaster"),
                    new OpeningHours("1100 - 1900"),
                    new Price("50"),
                    getTagSet("Amusement"),
                    getCommentSet("Expensive")),
            new Attraction(new Name("Gardens by the Bay"),
                    new Priority("7"),
                    new Contact("testcontact@gmail.com"),
                    new Address("18 Marina Gardens Dr, Singapore 018953"),
                    new Activities("see supertree"),
                    new OpeningHours("1200 - 1800"),
                    new Price("20"),
                    getTagSet("Park"),
                    getCommentSet("Expensive")),
            new Attraction(new Name("Singapore Botanic Gardens"),
                    new Priority("6"),
                    new Contact("testcontact@gmail.com"),
                    new Address("1 Cluny R, Singapore 259569"),
                    new Activities("take photos of orchids"),
                    new OpeningHours("0800 - 1900"),
                    new Price("20"),
                    getTagSet("Park", "Historical"),
                    getCommentSet("Expensive")),
            new Attraction(new Name("Marina Bay Sands Singapore"),
                    new Priority("1"),
                    new Contact("testcontact@gmail.com"),
                    new Address("10 Bayfront Ave, Singapore 018956"),
                    new Activities("sightseeing"),
                    new OpeningHours("1100 - 2200"),
                    new Price("100"),
                    getTagSet("Hotel"),
                    getCommentSet("Expensive")),
            new Attraction(new Name("Singapore Zoo"),
                    new Priority("2"),
                    new Contact("testcontact@gmail.com"),
                    new Address("80 Mandai Lake Rd, 729826"),
                    new Activities("see an elephant"),
                    new OpeningHours("1200 - 1800"),
                    new Price("30"),
                    getTagSet("Zoo"),
                    getCommentSet("Expensive")),
            new Attraction(new Name("Singapore Flyer"),
                    new Priority("3"),
                    new Contact("testcontact@gmail.com"),
                    new Address("30 Raffles Ave, Singapore 039803"),
                    new Activities("take pictures of view"),
                    new OpeningHours("1100 - 2000"),
                    new Price("20"),
                    getTagSet("Ride"),
                    getCommentSet("Expensive"))
        };
    }

    public static Location[] getSampleLocations(Attraction[] attractions) {
        return new Location[] {
            new Location(new LocationName("Singapore City"),
                    List.of(attractions[0], attractions[1], attractions[2])),
            new Location(new LocationName("Sentosa"),
                    List.of(attractions[0])),
            new Location(new LocationName("Mandai"),
                    List.of(attractions[4]))
        };
    }

    public static ReadOnlyMaplet getSampleMaplet() {
        Maplet sampleAb = new Maplet();
        Attraction[] sampleAttractions = getSampleAttractions();
        for (Attraction sampleAttraction : sampleAttractions) {
            sampleAb.addAttraction(sampleAttraction);
        }
        for (Itinerary itinerary : getSampleItineraries(sampleAttractions)) {
            sampleAb.addItinerary(itinerary);
        }
        for (Location sampleLocation : getSampleLocations(sampleAttractions)) {
            sampleAb.addLocation(sampleLocation);
        }
        return sampleAb;
    }

    public static Itinerary[] getSampleItineraries(Attraction[] attractions) {
        return new Itinerary[] {
            new Itinerary(new ItineraryName("Singapore"),
                    LocalDateTime.of(2025, 10, 21, 9, 0),
                    List.of(attractions[0], attractions[1], attractions[2])),
            new Itinerary(new ItineraryName("Singapore 2"),
                    LocalDateTime.of(2025, 10, 21, 10, 0),
                    List.of(attractions[3], attractions[4], attractions[5]))
        };
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a comment set containing the list of comments given.
     */
    public static Set<Comment> getCommentSet(String... strings) {
        return Arrays.stream(strings)
                .map(Comment::new)
                .collect(Collectors.toSet());
    }



}
