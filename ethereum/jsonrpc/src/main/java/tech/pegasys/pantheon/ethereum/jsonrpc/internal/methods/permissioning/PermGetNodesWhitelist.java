/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.pantheon.ethereum.jsonrpc.internal.methods.permissioning;

import tech.pegasys.pantheon.ethereum.jsonrpc.RpcMethod;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.JsonRpcRequest;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.methods.JsonRpcMethod;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcError;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcErrorResponse;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcResponse;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcSuccessResponse;
import tech.pegasys.pantheon.ethereum.p2p.network.exceptions.P2PDisabledException;
import tech.pegasys.pantheon.ethereum.permissioning.NodeLocalConfigPermissioningController;

import java.util.List;
import java.util.Optional;

public class PermGetNodesWhitelist implements JsonRpcMethod {

  private final Optional<NodeLocalConfigPermissioningController>
      nodeWhitelistPermissioningController;

  public PermGetNodesWhitelist(
      final Optional<NodeLocalConfigPermissioningController> nodeWhitelistPermissioningController) {
    this.nodeWhitelistPermissioningController = nodeWhitelistPermissioningController;
  }

  @Override
  public String getName() {
    return RpcMethod.PERM_GET_NODES_WHITELIST.getMethodName();
  }

  @Override
  public JsonRpcResponse response(final JsonRpcRequest req) {
    try {
      if (nodeWhitelistPermissioningController.isPresent()) {
        final List<String> enodeList =
            nodeWhitelistPermissioningController.get().getNodesWhitelist();

        return new JsonRpcSuccessResponse(req.getId(), enodeList);
      } else {
        return new JsonRpcErrorResponse(req.getId(), JsonRpcError.NODE_WHITELIST_NOT_ENABLED);
      }
    } catch (P2PDisabledException e) {
      return new JsonRpcErrorResponse(req.getId(), JsonRpcError.P2P_DISABLED);
    }
  }
}
