package edu.uci.ics.luisae.service.gateway.threadpool;

import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool
{
    private int numWorkers;

    private ArrayList<Worker> workers;
    private BlockingQueue<ClientRequest> queue;

    /*
     * BlockingQueue is a interface that allows us
     * to choose the type of implementation of the queue.
     * In this case we are using a LinkedBlockingQueue.
     *
     * BlockingQueue as the name implies will block
     * any thread requesting from it if the queue is empty
     * but only if you use the correct function
     */
    private ThreadPool(int numWorkers)
    {
        this.numWorkers = numWorkers;

        workers = new ArrayList<>();
        queue = new LinkedBlockingQueue<>();

        // TODO more work is needed to create the threads
        for(int i = 0; i < this.numWorkers; i++){
            workers.add(Worker.CreateWorker(i, this));
            workers.get(i).start();
        }
    }

    public static ThreadPool createThreadPool(int numWorkers)
    {
        return new ThreadPool(numWorkers);
    }

    /*
     * Note that this function only has package scoped
     * as it should only be called with the package by
     * a worker
     * 
     * Make sure to use the correct functions that will
     * block a thread if the queue is unavailable or empty
     */
    ClientRequest takeRequest()
    {
        // TODO *take* the request from the queue
        try{
            return this.queue.take();
        }
        catch(InterruptedException e){
            ServiceLogger.LOGGER.warning("TAKE: Interrupted while waiting ");
            ServiceLogger.LOGGER.warning(e.getMessage());
            ServiceLogger.LOGGER.warning(e.getLocalizedMessage());
            return null;
        }
    }

    public void putRequest(ClientRequest request)
    {
        // TODO *put* the request into the queue
        try{
            this.queue.put(request);
        }catch(InterruptedException e){
            ServiceLogger.LOGGER.warning("TAKE: Interrupted while waiting ");
            ServiceLogger.LOGGER.warning(e.getMessage());
            ServiceLogger.LOGGER.warning(e.getLocalizedMessage());
        }
    }

}
