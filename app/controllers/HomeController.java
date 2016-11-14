package controllers;

import models.Campaign;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final JPAApi jpaApi;

    @Inject
    public HomeController(JPAApi api) {
        this.jpaApi = api;
    }

    @Transactional
    public Result all() {
        Query query = jpaApi.em().createQuery("SELECT e FROM Campaign e");
        List campaigns = query.getResultList();
        final StringBuilder builder = new StringBuilder();
        campaigns.forEach((campaign) -> builder.append(Json.toJson(campaign)).append("\n"));
        return ok(builder.toString());
    }

    @Transactional
    public Result add(String name) {
        EntityManager em = jpaApi.em();
        Campaign campaign = new Campaign(name);
        em.persist(campaign);
        return ok("added : " + name);
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Willkommenn!"));
    }

}
