package org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.transformers

import org.jetbrains.magicmetamodel.impl.workspacemodel.impl.updaters.WorkspaceModelEntity

internal interface WorkspaceModelEntityBaseTransformer<in T, out R> {

  fun transform(inputEntity: T): R
}

internal interface WorkspaceModelEntityTransformer<in T, out R : WorkspaceModelEntity> :
  WorkspaceModelEntityBaseTransformer<T, R> {

  fun transform(inputEntities: Collection<T>): Collection<R> =
    inputEntities.map(this::transform).distinct()

  override fun transform(inputEntity: T): R
}

internal interface WorkspaceModelEntityPartitionTransformer<in T, out R : WorkspaceModelEntity> :
  WorkspaceModelEntityBaseTransformer<T, Collection<R>> {

  fun transform(inputEntities: Collection<T>): Collection<R> =
    inputEntities.flatMap(this::transform).distinct()

  override fun transform(inputEntity: T): Collection<R>
}
