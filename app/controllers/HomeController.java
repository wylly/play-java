package controllers;

import models.Campaign;
import play.data.DynamicForm;
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
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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

    @Transactional
    public Result deleteCampaign(String id) {
        Campaign campaign = jpaApi.em().find(Campaign.class, Long.parseLong(id));
        jpaApi.em().remove(campaign);
        return redirect(routes.HomeController.index());
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
        DynamicForm form = formFactory.form().bindFromRequest();
        Map<String, String> formData = mergeCategoriesInForm(form.data());
        System.out.println(formData.keySet().stream().reduce("",(a,b)->a+" "+b));
        Campaign campaign = formFactory.form(Campaign.class).bind(formData).get();
        EntityManager em = jpaApi.em();
        em.persist(campaign);
        return redirect(routes.HomeController.index());
    }

    private Map<String, String> mergeCategoriesInForm(Map<String, String> formData) {
        List<String> categoriesKeys = formData.keySet().stream()
                .filter(key -> key.contains("categories"))
                .collect(Collectors.toList());
        StringJoiner sj = new StringJoiner(", ");
        for (String key : categoriesKeys) {
            sj.add(formData.get(key));
            formData.remove(key);
        }
        formData.put("categories",sj.toString());
        return formData;
    }

    @Transactional
    public Result save(long id) {
        DynamicForm form = formFactory.form().bindFromRequest();
        Map<String, String> formData = mergeCategoriesInForm(form.data());
        Campaign campaign = formFactory.form(Campaign.class).bind(formData).get();
        campaign.setCampaignid(id);
        jpaApi.em().merge(campaign);
        return redirect(routes.HomeController.index());
    }

    @Transactional
    public Result edit(long id) {
        return index(false, jpaApi.em().find(Campaign.class, id));
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Transactional
    public Result index() {
        return index(true, new Campaign());
    }

    @Transactional
    public Result index(boolean isNew, Campaign campaign) {
        return ok(index.render(getAllCampaigns(), isNew, campaign));
    }
}
