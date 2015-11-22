/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.bean.ProjectContainer;
import org.goko.core.workspace.bean.ProjectContainerUiProvider;
import org.goko.gcode.rs274ngcv3.ui.workspace.IRS274WorkspaceService;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.AddExecutionQueueAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.DeleteGCodeProviderAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider.ModifierSubMenu;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier.DeleteModifierAction;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier.EnableDisableAction;

/**
 * @author PsyKo
 * @date 31 oct. 2015
 */
public class GCodeContainerUiProvider extends ProjectContainerUiProvider {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GCodeContainerUiProvider.class);	
	/** GCode service */
	private IRS274NGCService rs274Service;
	private IRS274WorkspaceService rs274WorkspaceService;
	private IExecutionService<?, ?> executionService;
	private IStyledLabelProvider labelProvider;

	/**
	 * @param rs274Service 
	 * @param type
	 */
	public GCodeContainerUiProvider(IRS274NGCService rs274Service, IRS274WorkspaceService rs274WorkspaceService, IExecutionService<?, ?> executionService) {
		super("TEST");
		this.rs274Service = rs274Service;
		this.rs274WorkspaceService = rs274WorkspaceService;
		this.labelProvider = new GCodeContainerLabelProvider();
		this.executionService = executionService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesLabelFor(java.lang.Object)
	 */
	@Override
	public boolean providesLabelFor(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return (content instanceof IGCodeProvider)
			|| (content instanceof IModifier);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getStyledText(java.lang.Object)
	 */
	@Override
	public StyledString getStyledText(Object element) {		
		return labelProvider.getStyledText(element);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {		
		return labelProvider.getImage(element);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesContentFor(java.lang.Object)
	 */
	@Override
	public boolean providesContentFor(Object content) throws GkException {
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return content instanceof GCodeProvider;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object content) throws GkException {
		if(content instanceof GCodeProvider){
			return CollectionUtils.isNotEmpty(rs274Service.getModifierByGCodeProvider(((GCodeProvider) content).getId()));
		}else if(content instanceof ProjectContainer){			
			return CollectionUtils.isNotEmpty(rs274Service.getGCodeProvider());			
		}
		return false;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object content) throws GkException {
		if(content instanceof GCodeProvider){
			return rs274Service.getModifierByGCodeProvider(((GCodeProvider) content).getId()).toArray();
		}else if(content instanceof ProjectContainer){			
			return rs274Service.getGCodeProvider().toArray();			
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#getParent(java.lang.Object)
	 */
	@Override
	public Object getParent(Object content) throws GkException {		
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesMenuFor(java.lang.Object)
	 */
	@Override
	public boolean providesMenuFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof ProjectContainer){
			return StringUtils.equals(getType(), ((ProjectContainer) content).getType());
		}
		return (content instanceof GCodeProvider)
			|| (content instanceof IModifier);
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createMenuFor(org.eclipse.jface.action.IMenuManager, java.lang.Object)
	 */
	@Override
	public void createMenuFor(IMenuManager contextMenu, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		if(content instanceof GCodeProvider){
			createMenuForGCodeProvider(contextMenu, (GCodeProvider)content);
		}else if(content instanceof IModifier<?>){
			createMenuForGCodeModifier(contextMenu, (IModifier<?>)content);
		}
	}
	
	private void createMenuForGCodeModifier(IMenuManager contextMenu, IModifier<?> modifier) {
		contextMenu.add(new EnableDisableAction(rs274Service, modifier.getId()));
		contextMenu.add(new Separator());
		contextMenu.add(new DeleteModifierAction(rs274Service, modifier.getId()));		
	}

	protected void createMenuForGCodeProvider(IMenuManager contextMenu, final GCodeProvider content) throws GkException {
		// Submenu for a specific user
        MenuManager subMenu = new ModifierSubMenu(rs274Service, rs274WorkspaceService, content.getId());
        contextMenu.add(subMenu); 
       
        contextMenu.add(new Separator());
        contextMenu.add(new AddExecutionQueueAction(rs274Service, executionService, content.getId()));
        contextMenu.add(new Separator());
        contextMenu.add(new DeleteGCodeProviderAction(rs274Service, content.getId()));		
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#providesConfigurationPanelFor(java.lang.Object)
	 */
	@Override
	public boolean providesConfigurationPanelFor(ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		
		if(content instanceof IModifier<?>){
			IModifier<?> iModifier = (IModifier<?>) content;
			
			List<IModifierUiProvider<GCodeProvider, ?>> lstBuilders = rs274WorkspaceService.getModifierBuilder();
			if(CollectionUtils.isNotEmpty(lstBuilders)){
				for (IModifierUiProvider<GCodeProvider, ?> iModifierUiProvider : lstBuilders) {
					if(iModifierUiProvider.providesConfigurationPanelFor(iModifier)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.ProjectContainerUiProvider#createConfigurationPanelFor(org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	public void createConfigurationPanelFor(Composite parent, ISelection selection) throws GkException {
		IStructuredSelection strSelection = (IStructuredSelection) selection;
		Object content = strSelection.getFirstElement();
		
		if(content instanceof IModifier<?>){
			IModifier<?> iModifier = (IModifier<?>) content;
			
			List<IModifierUiProvider<GCodeProvider, ?>> lstBuilders = rs274WorkspaceService.getModifierBuilder();
			if(CollectionUtils.isNotEmpty(lstBuilders)){
				for (IModifierUiProvider<GCodeProvider, ?> iModifierUiProvider : lstBuilders) {
					if(iModifierUiProvider.providesConfigurationPanelFor(iModifier)){
						iModifierUiProvider.createConfigurationPanelFor(parent, iModifier);
						break;
					}
				}
			}
		}
	}
	
}
