package controllers;

import models.Campaign;
import play.data.Form;
import play.data.FormFactory;
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
    private final FormFactory formFactory;

    @Inject
    public HomeController(JPAApi jpaApi, FormFactory formFactory) {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }



    public Result all() {
        List<Campaign> campaigns = getAllCampaigns();
        final StringBuilder builder = new StringBuilder();
        campaigns.forEach((campaign) -> builder.append(Json.toJson(campaign)).append("\n"));
        return ok(builder.toString());
    }

    @Transactional
    private List<Campaign> getAllCampaigns() {
        Query query = jpaApi.em().createQuery("SELECT e FROM Campaign e");
        return query.getResultList();
    }

    @Transactional
    public Result add() {
        Form<Campaign> campaignForm = formFactory.form(Campaign.class);
        System.out.println(campaignForm.toString());
        Campaign campaign = campaignForm.bindFromRequest().get();
        EntityManager em = jpaApi.em();
        em.persist(campaign);
        return redirect(routes.HomeController.index());
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Transactional
    public Result index() {
        return ok(index.render(getAllCampaigns()));
    }

}
