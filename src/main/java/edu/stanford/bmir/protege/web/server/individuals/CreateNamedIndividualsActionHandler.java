package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsActionHandler extends AbstractHasProjectActionHandler<CreateNamedIndividualsAction, CreateNamedIndividualsResult> {

    @Inject
    public CreateNamedIndividualsActionHandler(OWLAPIProjectManager projectManager,
                                               AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(EDIT_ONTOLOGY, CREATE_INDIVIDUAL);
    }

    @Override
    protected CreateNamedIndividualsResult execute(CreateNamedIndividualsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        ChangeApplicationResult<Set<OWLNamedIndividual>> result = project.applyChanges(executionContext.getUserId(), new CreateIndividualsChangeListGenerator(action.getShortNames(), action.getType()), new FixedMessageChangeDescriptionGenerator<Set<OWLNamedIndividual>>("Created individuals"));
        Set<OWLNamedIndividual> individuals = result.getSubject().get();
        Set<OWLNamedIndividualData> individualData = new HashSet<OWLNamedIndividualData>();
        for(OWLNamedIndividual individual : individuals) {
            individualData.add(project.getRenderingManager().getRendering(individual));
        }
        return new CreateNamedIndividualsResult(individualData);
    }

    @Override
    public Class<CreateNamedIndividualsAction> getActionClass() {
        return CreateNamedIndividualsAction.class;
    }
}
