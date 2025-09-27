package simulation.big;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

import models.Recipe;
import models.Scheduler.Scheduler;
import models.Scheduler.complex.RoundRobinScheduler;
import models.Scheduler.ordered.BestFitScheduler;
import models.Scheduler.ordered.FirstFitScheduler;
import models.Scheduler.ordered.RandomScheduler;
import models.Scheduler.ordered.WorstFitScheduler;
import models.Station.PriorityStation;
import models.Station.Station;

public class Main
{
    private static Recipe[] generate_recipes_array(int count)
    {
        Recipe[] recipes = new Recipe[] {
            new Recipe(
            ProductA1.class, 
                ProductA2.class, 
                ProductA3.class,
                ProductA4.class
            ),
            new Recipe(ProductA1.class, ProductA2.class),
            new Recipe(
            ProductB1.class,
                ProductB2.class,
                ProductB3.class
            ),
            new Recipe(ProductB1.class, ProductB2.class)
        };

        Recipe[] totalRecipes = new Recipe[count];
        Random rand = new Random(1);

        for (int i = 0; i < count; i++)
        {
            totalRecipes[i] = recipes[rand.nextInt(recipes.length)];
        }

        return totalRecipes;
    }

    private static int[] generate_priorities_array(int count)
    {
        int[] priorities =  new int[count];
        Random rand = new Random(100);

        for (int i = 0; i < count; i++)
        {
            priorities[i] = rand.nextInt(count - 1);
        }

        return priorities;
    }

    private static double sim (
        Scheduler sched, 
        int receip_count,
        int stationsPerType, 
        boolean print_stations_usage
    ) {
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < stationsPerType; i++)
        {
            stations.add(new PriorityStation(ProductA1.class, 7));
            stations.add(new PriorityStation(ProductA2.class, 7));
            stations.add(new PriorityStation(ProductA3.class, 7));
            stations.add(new PriorityStation(ProductA4.class, 7));
            
            stations.add(new PriorityStation(ProductB1.class, 6));
            stations.add(new PriorityStation(ProductB2.class, 6));
            stations.add(new PriorityStation(ProductB3.class, 6));
        }

        stations.sort(Comparator.naturalOrder());
        for (var station : stations)
        {
            sched.AddStation(station);
        }

        var recipes = generate_recipes_array(receip_count);
        var priorities = generate_priorities_array(receip_count);
        var recipes_index = new AtomicInteger();

        List<Thread> threads = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(receip_count);

        for (int i = 0; i < receip_count; i++)
        {
            threads.add(new Thread(() -> {
                int index = recipes_index.getAndIncrement();
                Recipe recipe = recipes[index];
                int priority = priorities[index];

                recipe.SendToScheduler(sched, priority);
                latch.countDown();
            }));
        }

        Collections.shuffle(threads);
        long simStart = System.currentTimeMillis();
        threads.stream().forEach(t -> t.start());

        try {
            latch.await();
        } catch (InterruptedException ex) {
            System.err.println("Simulation stopped.");
        }

        double duration = (double)(System.currentTimeMillis() - simStart) / 1000;

        if (print_stations_usage)
        {
            Map<String, List<Station>> stationByProducedType = stations
                .stream()
                .collect(Collectors.groupingBy(s -> s.producedProduct.getTypeName()))
            ;

            stationByProducedType
                .keySet()
                .stream()
                .sorted()    
                .forEach(typeName -> {
                    List<String> usage = stationByProducedType.get(typeName)
                        .stream()
                        .map(s -> String.valueOf(s.completedTasks()))
                        .toList()
                    ;
                    System.out.println("\t" + typeName + ": " + String.join("-", usage));
                })
            ;
        }
        
        sched.ShutStations();
        return duration;
    }

    private static final String OUTPUT_FILE_NAME = "output.csv";

    private static void print_start_row(String... schedulers)
    {
        try (PrintWriter file = new PrintWriter(OUTPUT_FILE_NAME))
        {
            file.println("Size;Size/Stations;" + String.join(";", schedulers));
        } catch (IOException e)
        {
            e.printStackTrace();
        }     
    }

    private static void print_row(int recipes, String... times)
    {
        try (PrintWriter file = new PrintWriter(
            new FileWriter(OUTPUT_FILE_NAME, true)
        ))
        {
            String size_on_stations = String.format(
                Locale.ITALIAN, 
                "%.3f", 
                ((float)recipes) / STATIONS_PER_PRODUCT_TYPE
            );
            file.println(
                recipes + ";" + 
                size_on_stations + ";" +
                String.join(";", times));
        } catch (IOException e)
        {
            e.printStackTrace();
        }     
    }

    private static final int STATIONS_PER_PRODUCT_TYPE = 5;
    private static final boolean PRINT_STATIONS_USAGE = false;

    public static void main(String[] args)
    {
        Recipe.Debug = false;
        Station.Debug = false;

        int[] recipes_count = new int[] {
            10,
            20, 
            50,
            100,
            200,
            500,
            1000,
            5000,
            10000
        };

        print_start_row("Random", "FirstFit", "BestFit", "WorstFit", "RoundRobin");
        for (int recipe_count : recipes_count)
        {
            System.out.println("Simulating with " + recipe_count + " recipes.");

            double[] durations = new double[] {
                sim(new RandomScheduler(),     recipe_count, STATIONS_PER_PRODUCT_TYPE, PRINT_STATIONS_USAGE),
                sim(new FirstFitScheduler(),   recipe_count, STATIONS_PER_PRODUCT_TYPE, PRINT_STATIONS_USAGE),
                sim(new BestFitScheduler(),    recipe_count, STATIONS_PER_PRODUCT_TYPE, PRINT_STATIONS_USAGE),
                sim(new WorstFitScheduler(),   recipe_count, STATIONS_PER_PRODUCT_TYPE, PRINT_STATIONS_USAGE),
                sim(new RoundRobinScheduler(), recipe_count, STATIONS_PER_PRODUCT_TYPE, PRINT_STATIONS_USAGE)
            };

            String[] durations_strings = Arrays
                .stream(durations)
                .mapToObj(d -> String.format(Locale.ITALIAN, "%.4f", d))
                .toArray(String[]::new)
            ;

            for (String duration : durations_strings)
            {
                System.out.println("\t" + duration + " s");
            }
            print_row(recipe_count, durations_strings);
            System.out.println();
        }  
    }
}
