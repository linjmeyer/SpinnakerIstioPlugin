package com.redbox.spinnaker.istio

import com.netflix.spinnaker.kork.plugins.api.PluginSdks
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.netflix.spinnaker.orca.clouddriver.tasks.MonitorKatoTask
import com.netflix.spinnaker.orca.clouddriver.tasks.artifacts.CleanupArtifactsTask
import com.netflix.spinnaker.orca.clouddriver.tasks.manifest.DeployManifestTask
import com.netflix.spinnaker.orca.clouddriver.tasks.manifest.PromoteManifestKatoOutputsTask
import com.netflix.spinnaker.orca.clouddriver.tasks.manifest.ResolveDeploySourceManifestTask
import com.netflix.spinnaker.orca.clouddriver.tasks.manifest.WaitForManifestStableTask
import com.netflix.spinnaker.orca.pipeline.tasks.artifacts.BindProducedArtifactsTask
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import com.netflix.spinnaker.orca.api.pipeline.graph.StageDefinitionBuilder;
import javax.annotation.Nonnull;


class IstioStagePlugin(wrapper: PluginWrapper): Plugin(wrapper) {

    override fun start() {
        println(IstioStagePlugin::class.simpleName + " starting")
    }

    override fun stop() {
        println(IstioStagePlugin::class.simpleName + " stopping")
    }
}

@Extension
class IstioDeployRoutesStage(val pluginSdks: PluginSdks, val configuration: PluginConfig) : StageDefinitionBuilder {

    override fun taskGraph(@Nonnull stage: StageExecution, @Nonnull builder: TaskNode.Builder) {
        builder
                .withTask(ResolveDeploySourceManifestTask.TASK_NAME, ResolveDeploySourceManifestTask::class.java)
                .withTask(DeployManifestTask.TASK_NAME, DeployManifestTask::class.java)
                .withTask("monitorDeploy", MonitorKatoTask::class.java)
                .withTask(PromoteManifestKatoOutputsTask.TASK_NAME, PromoteManifestKatoOutputsTask::class.java)
                .withTask(WaitForManifestStableTask.TASK_NAME, WaitForManifestStableTask::class.java)
                .withTask(CleanupArtifactsTask.TASK_NAME, CleanupArtifactsTask::class.java)
                .withTask("monitorCleanup", MonitorKatoTask::class.java)
                .withTask(PromoteManifestKatoOutputsTask.TASK_NAME, PromoteManifestKatoOutputsTask::class.java)
                .withTask(BindProducedArtifactsTask.TASK_NAME, BindProducedArtifactsTask::class.java)
    }

}
