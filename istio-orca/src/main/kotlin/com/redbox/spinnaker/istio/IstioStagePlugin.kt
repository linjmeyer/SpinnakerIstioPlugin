package com.redbox.spinnaker.istio

import com.netflix.spinnaker.kork.plugins.api.PluginSdks
import com.netflix.spinnaker.kork.plugins.api.ExtensionConfiguration;
import com.netflix.spinnaker.orca.api.pipeline.graph.TaskNode
import com.netflix.spinnaker.orca.api.pipeline.models.StageExecution
import com.netflix.spinnaker.orca.api.pipeline.Task;
import com.netflix.spinnaker.orca.api.pipeline.TaskResult
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
import org.slf4j.LoggerFactory
import javax.annotation.Nonnull;


public class IstioStagePlugin(wrapper: PluginWrapper): Plugin(wrapper) {

    private val logger = LoggerFactory.getLogger(IstioStagePlugin::class.java)

    override fun start() {
        logger.info(IstioStagePlugin::class.simpleName + " starting")
    }

    override fun stop() {
        logger.info(IstioStagePlugin::class.simpleName + " stopping")
    }
}

@Extension()
public class IstioDeployRoutes(val pluginSdks: PluginSdks, val configuration: PluginConfig) : StageDefinitionBuilder {

    private val logger = LoggerFactory.getLogger(IstioDeployRoutes::class.java)

    override fun taskGraph(@Nonnull stage: StageExecution, @Nonnull builder: TaskNode.Builder) {
        logger.info("Building task graph")
        builder.withTask(IstioIncreaseTraffic.TASK_NAME, IstioIncreaseTraffic::class.java)
        // Copy pasted from native Spinnaker Deploy Manifest stage:
//        builder
//                .withTask(ResolveDeploySourceManifestTask.TASK_NAME, ResolveDeploySourceManifestTask::class.java)
//                .withTask(DeployManifestTask.TASK_NAME, DeployManifestTask::class.java)
//                .withTask("monitorDeploy", MonitorKatoTask::class.java)
//                .withTask(PromoteManifestKatoOutputsTask.TASK_NAME, PromoteManifestKatoOutputsTask::class.java)
//                .withTask(WaitForManifestStableTask.TASK_NAME, WaitForManifestStableTask::class.java)
//                .withTask(CleanupArtifactsTask.TASK_NAME, CleanupArtifactsTask::class.java)
//                .withTask("monitorCleanup", MonitorKatoTask::class.java)
//                .withTask(PromoteManifestKatoOutputsTask.TASK_NAME, PromoteManifestKatoOutputsTask::class.java)
//                .withTask(BindProducedArtifactsTask.TASK_NAME, BindProducedArtifactsTask::class.java)
    }
}

class IstioIncreaseTraffic : Task {

    private val logger = LoggerFactory.getLogger(IstioIncreaseTraffic::class.java)

    override fun execute(stage: StageExecution): TaskResult {
        logger.info(IstioIncreaseTraffic::class.simpleName + " has started")
        Thread.sleep(5000);
        return TaskResult.SUCCEEDED
    }

    companion object {
        const val TASK_NAME = "IstioIncreaseTraffic"
    }
}

